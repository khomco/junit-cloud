**junit-cloud**

```java
public class CloudServerBuildTest {

  @Rule
  public CloudServerBuilder cloudBuilderRule = new CloudServerBuilder<Server, CenturyLinkCloudServerIdentifierStrategy>() {
      @Override
      protected void initialize() {
          setAdapter(new CenturyLinkCloudAdapter(client, accountAlias, groupId));
          setThreadCount(4);
      }
  };
  
  @DeployServers({
    @ServerInfo(name = "TST1",
            description = "Testing deployment of Managed RHEL server",
            os = "RHEL-6-64-TEMPLATE", processors = 1, memory = 2, attributes = {
            @ServerAttribute(name = "type", value = "standard"),
            @ServerAttribute(name = "storageType", value = "standard"),
            @ServerAttribute(name = "isManagedOS", value = "true"),
            @ServerAttribute(name = "hyperscale", value = "false"),
    }),
    @ServerInfo(name = "TST2",
            description = "Testing deployment of Unmanaged RHEL server",
            os = "RHEL-6-64-TEMPLATE", processors = 1, memory = 2, attributes = {
            @ServerAttribute(name = "type", value = "standard"),
            @ServerAttribute(name = "storageType", value = "standard"),
            @ServerAttribute(name = "isManagedOS", value = "false"),
            @ServerAttribute(name = "hyperscale", value = "false"),
    })
  })
  @Test
  public void testBuildServer() throws ExecutionException, InterruptedException {
      DeployedServersCollection deployedServers = cloudBuilderRule.getDeployedServers();
      List servers = deployedServers.getServers();

      assertEquals(2, servers.size());

  }
}
```
