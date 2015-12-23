package com.homconet.junit.cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by khomco on 12/22/15.
 */
public class DeployedServersCollection<T, S> {

    private final Map<Future<S>, Future<T>> servers;
    private T[] completedServers;

    public DeployedServersCollection(Map<Future<S>, Future<T>> servers) {
        this.servers = servers;
    }

    public List<T> getServers() throws ExecutionException, InterruptedException {
        ArrayList<T> serverList = new ArrayList<>();
        for(Future<T> server : servers.values()) {
            serverList.add(server.get());

        }
        return serverList;
    }
}
