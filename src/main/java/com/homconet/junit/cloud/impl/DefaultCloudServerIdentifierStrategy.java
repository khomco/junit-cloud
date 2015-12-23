package com.homconet.junit.cloud.impl;

import com.homconet.junit.cloud.CloudServerIdentifierStrategy;

/**
 * Created by khomco on 12/22/15.
 */
public class DefaultCloudServerIdentifierStrategy implements CloudServerIdentifierStrategy<DefaultCloudServerIdentifier> {

    private final DefaultCloudServerIdentifier serverIdentifier;

    public DefaultCloudServerIdentifierStrategy(DefaultCloudServerIdentifier serverIdentifier) {
        this.serverIdentifier = serverIdentifier;
    }

    @Override
    public DefaultCloudServerIdentifier getServerIdentifier() {
        return serverIdentifier;
    }
}
