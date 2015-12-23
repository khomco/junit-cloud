package com.homconet.junit.cloud.impl;

import com.homconet.junit.cloud.CloudServerIdentifier;

/**
 * Created by khomco on 12/22/15.
 */
public class DefaultCloudServerIdentifier implements CloudServerIdentifier{

    private final String serverId;

    public DefaultCloudServerIdentifier(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }
}
