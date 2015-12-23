package com.homconet.junit.cloud;

import com.homconet.junit.cloud.annotations.DeployServers;
import com.homconet.junit.cloud.annotations.ServerAttribute;
import com.homconet.junit.cloud.annotations.ServerInfo;
import com.homconet.junit.cloud.impl.DefaultCloudServerRequest;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by khomco on 12/22/15.
 */
public abstract class CloudServerBuilder<T, S> extends TestWatcher {

    private static final int DEFAULT_THREAD_COUNT = 2;

    private CloudServerAdapter<T, S> adapter;
    private DeployedServersCollection<T, S> cloudServers;
    private ExecutorService executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_COUNT);
    private boolean assureServersDestroyed = false;

    abstract protected void initialize();

    protected void setAdapter(CloudServerAdapter<T, S> cloudServerAdapter) {
        adapter = cloudServerAdapter;
    }

    protected void setThreadCount(int threadCount) {
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    protected void assureServersDestroyed(boolean assureServersDestroyed) {
        this.assureServersDestroyed = assureServersDestroyed;
    }

    @Override
    protected void starting(Description description) {
        initialize();

        DeployServers servers = description.getAnnotation(DeployServers.class);
        if (servers == null) {
            return;
        }

        ArrayList<Future<T>> serverRequestList = new ArrayList<>();
        Map<Future<S>, Future<T>> serverRequestsMap = new HashMap<>();
        for (ServerInfo serverInfo : servers.value()) {
            DefaultCloudServerRequest serverRequest = new DefaultCloudServerRequest();
            serverRequest.setName(serverInfo.name());
            serverRequest.setDescription(serverInfo.description());
            serverRequest.setOperatingSystem(serverInfo.os());
            serverRequest.setMemory(serverInfo.memory());
            serverRequest.setProcessorCount(serverInfo.processors());
            for(ServerAttribute attribute : serverInfo.attributes()) {
                serverRequest.addAttribute(attribute.name(), attribute.value());
            }

            Future<S> cloudServerIdentifierStrategy = asyncDeployServer(serverRequest);
            Future<T> serverCompleteFuture = asyncWaitForServerComplete(cloudServerIdentifierStrategy);
            try {
                serverRequestsMap.put(cloudServerIdentifierStrategy, serverCompleteFuture);
            } catch (Exception e) {
                //TODO: do something maybe
            }
            serverRequestList.add(serverCompleteFuture);
        }
        cloudServers = new DeployedServersCollection<T, S>(serverRequestsMap);
    }

    private Future<S> asyncDeployServer(CloudServerRequest serverRequest) {
        return executorService.submit(() -> waitForServerDeploy(serverRequest));
    }

    private S waitForServerDeploy(CloudServerRequest serverRequest) {
        long start = System.currentTimeMillis();
        while (adapter.getTimeout() > (System.currentTimeMillis() - start)) {
            S cloudServerIdentifierStrategy = adapter.deployServer(serverRequest);
            if (cloudServerIdentifierStrategy != null) {
                return cloudServerIdentifierStrategy;
            }

            try {
                Thread.sleep(adapter.getWait());
            } catch (Exception e) {
            }
        }

        throw new CloudServerDeployTimeoutException(
                String.format("Failed to deploy cloud server with requested name %s within the timeout of %s",
                        serverRequest.getName(), adapter.getTimeout()));
    }

    private Future<T> asyncWaitForServerComplete(final Future<S> cloudServerIdentifierStrategy) {
        return executorService.submit(() -> waitForServerComplete(cloudServerIdentifierStrategy.get(adapter.getTimeout(), TimeUnit.MILLISECONDS)));
    }

    protected T waitForServerComplete(S cloudServerIdentifierStrategy) {
        long start = System.currentTimeMillis();
        while (adapter.getTimeout() > (System.currentTimeMillis() - start)) {
            T cloudServer = adapter.getServer(cloudServerIdentifierStrategy);
            if (cloudServer != null) {
                return cloudServer;
            }

            try {
                Thread.sleep(adapter.getWait());
            } catch (Exception e) {
            }
        }

        throw new CloudServerDeployTimeoutException(
                String.format("Failed to retrieve cloud server with identifier %s within the timeout of %s",
                        cloudServerIdentifierStrategy, adapter.getTimeout()));
    }

    @Override
    protected void finished(Description description) {
        try {
            for (T cloudServer : cloudServers.getServers()) {
                adapter.destroyServer(cloudServer);
            }
        } catch (Exception e) {
            if(assureServersDestroyed) {
                throw new IllegalStateException("Failed to destroy server", e);
            } else {
                e.printStackTrace();
            }
        }
    }

    public DeployedServersCollection<T, S> getDeployedServers() {
        return cloudServers;
    }
}
