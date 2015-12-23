package com.homconet.junit.cloud;

/**
 * Created by khomco on 12/22/15.
 */
public interface CloudServerAdapter<T, S> {

    S deployServer(CloudServerRequest request);

    T getServer(S cloudServerIdentifierStrategy);

    void destroyServer(T cloudServer);

    /**
     * Get configured timeout in milliseconds for the creating of
     * a cloud server.  This value will be used for
     * the calls to {@link #getServer(Object)}
     *
     * @return
     */
    long getTimeout();

    /**
     * Get configured wait time in milliseconds between
     * calls to {@link #getServer(Object)}
     *
     * @return
     */
    long getWait();
}
