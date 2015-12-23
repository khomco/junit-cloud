package com.homconet.junit.cloud;

import java.util.Map;

/**
 * Created by khomco on 12/22/15.
 */
public interface CloudServerRequest {

    String getName();

    String getDescription();

    String getOperatingSystem();

    int getMemory();

    int getProcessorCount();

    Map<String, String> getAttributes();
}
