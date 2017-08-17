package com.opsgenie.playground.scenarioGeneratorAgent.scenario.config.properties;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public class ScenarioConfigProperty {

    private String id;

    private String name;

    private String description;

    private int minTime = 60;

    private int maxTime = 180;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
