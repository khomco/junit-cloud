package com.homconet.junit.cloud;

/**
 * Created by khomco on 12/22/15.
 */
public interface CloudServerIdentifierStrategy<T extends CloudServerIdentifier> {

    T getServerIdentifier();
}
