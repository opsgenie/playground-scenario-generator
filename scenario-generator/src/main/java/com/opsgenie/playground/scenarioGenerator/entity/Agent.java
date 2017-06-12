package com.opsgenie.playground.scenarioGenerator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public class Agent {

    private String name;

    @JsonIgnore
    private String url;

    private String description;

    public Agent() {
    }

    public Agent(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Agent(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        if (name != null ? !name.equals(agent.name) : agent.name != null) return false;
        if (url != null ? !url.equals(agent.url) : agent.url != null) return false;
        return description != null ? description.equals(agent.description) : agent.description == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
