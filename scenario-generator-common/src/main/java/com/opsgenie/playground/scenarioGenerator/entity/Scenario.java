package com.opsgenie.playground.scenarioGenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This class might be abstract later but it is used directly by scenario generator as a class. If
 * we want to do something specific for apps later, we can make this abstract again, then agent
 * applications need to know about the type of the scenario and cast accordingly
 *
 * @author Serhat Can
 * @version 31/05/17
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {

    protected String name;

    protected String description;

    protected boolean enabled = false;

    protected int minTime = 60;

    protected int maxTime = 180;

    public Scenario() {
    }

    public Scenario(String name) {
        this.name = name;
    }

    public Scenario(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Scenario(String name, String description, boolean enabled, int minTime, int maxTime) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scenario scenario = (Scenario) o;

        if (enabled != scenario.enabled) return false;
        if (minTime != scenario.minTime) return false;
        if (maxTime != scenario.maxTime) return false;
        if (name != null ? !name.equals(scenario.name) : scenario.name != null) return false;
        return description != null ? description.equals(scenario.description) : scenario.description == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (enabled ? 1 : 0);
        result = 31 * result + minTime;
        result = 31 * result + maxTime;
        return result;
    }

    @Override
    public String toString() {
        return "Scenario{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", minTime=" + minTime +
                ", maxTime=" + maxTime +
                '}';
    }
}
