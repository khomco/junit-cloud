package com.homconet.junit.cloud;

/**
 * Created by khomco on 12/22/15.
 */
public class CloudServerDeployTimeoutException extends RuntimeException {
    public CloudServerDeployTimeoutException(String message) {
        super(message);
    }

    public CloudServerDeployTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudServerDeployTimeoutException(Throwable cause) {
        super(cause);
    }

    protected CloudServerDeployTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
