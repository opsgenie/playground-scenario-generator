package com.opsgenie.playground.scenarioGenerator.controller.dto;

/**
 * Created by Serhat Can
 */
public class StartScenarioRequest {

    private int requestedTime;

    public StartScenarioRequest(int requestedTime) {
        this.requestedTime = requestedTime;
    }

    public int getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(int requestedTime) {
        this.requestedTime = requestedTime;
    }
}
