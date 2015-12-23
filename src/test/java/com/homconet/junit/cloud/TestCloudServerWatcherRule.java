package com.homconet.junit.cloud;

import com.homconet.junit.cloud.annotations.DeployServers;
import com.homconet.junit.cloud.annotations.ServerAttribute;
import com.homconet.junit.cloud.annotations.ServerInfo;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Created by khomco on 12/22/15.
 */
public class TestCloudServerWatcherRule {

    private StubCloudServerAdapter stubCloudServerAdapter = new StubCloudServerAdapter();

    @Rule
    public CloudServerBuilder<SomeServerObject, StubCloudServerIdentifierStrategy> cloudServerBuilder = new CloudServerBuilder<SomeServerObject, StubCloudServerIdentifierStrategy>() {
        @Override
        protected void initialize() {
            setAdapter(stubCloudServerAdapter);
        }
    };

    private SomeServerObject expectedSomeServerObject = new SomeServerObject("testName");

    @DeployServers({
        @ServerInfo(name = "testName", description = "description", os = "ubuntu", processors = 1, memory = 2,
            attributes = {
                @ServerAttribute(name = "isManagedOs", value = "true")
            }
        )
    })
    @Test
    public void testSomething() throws ExecutionException, InterruptedException {
        DeployedServersCollection<SomeServerObject, StubCloudServerIdentifierStrategy> deployedServers = cloudServerBuilder.getDeployedServers();
        List<SomeServerObject> servers = deployedServers.getServers();

        assertEquals(1, servers.size());
        assertEquals(expectedSomeServerObject, servers.get(0));
    }

    private class StubCloudServerAdapter implements CloudServerAdapter<SomeServerObject, StubCloudServerIdentifierStrategy> {
        @Override
        public StubCloudServerIdentifierStrategy deployServer(CloudServerRequest request) {
            return new StubCloudServerIdentifierStrategy(new StubCloudServerIdentifier("identifier"));
        }

        @Override
        public SomeServerObject getServer(StubCloudServerIdentifierStrategy cloudServerIdentifierStrategy) {
            return null;
        }

        public SomeServerObject getServer(CloudServerIdentifierStrategy cloudServerIdentifierStrategy) {
            return expectedSomeServerObject;
        }

        public void destroyServer(SomeServerObject cloudServer) {

        }

        public long getTimeout() {
            return 5000L;
        }

        public long getWait() {
            return 5000L;
        }
    }

    private class SomeServerObject {

        private final String name;

        public SomeServerObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private class StubCloudServerIdentifier implements CloudServerIdentifier {
        private final String identifier;

        public StubCloudServerIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return identifier;
        }

    }
    private class StubCloudServerIdentifierStrategy implements CloudServerIdentifierStrategy<StubCloudServerIdentifier> {

        private final StubCloudServerIdentifier identifier;

        public StubCloudServerIdentifierStrategy(StubCloudServerIdentifier identifier) {
            this.identifier = identifier;
        }

        @Override
        public StubCloudServerIdentifier getServerIdentifier() {
            return identifier;
        }
    }
}
