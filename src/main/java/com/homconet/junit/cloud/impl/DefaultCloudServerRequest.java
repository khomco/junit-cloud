package com.homconet.junit.cloud.impl;

import com.homconet.junit.cloud.CloudServerRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by khomco on 12/22/15.
 */
public class DefaultCloudServerRequest implements CloudServerRequest {

    private String name;
    private String description;
    private String operatingSystem;
    private int memory;
    private int processorCount;
    private Map<String,String> attributes = new HashMap<>();

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getMemory() {
        return memory;
    }

    public void setProcessorCount(int processorCount) {
        this.processorCount = processorCount;
    }

    public int getProcessorCount() {
        return processorCount;
    }

    protected void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }
}
