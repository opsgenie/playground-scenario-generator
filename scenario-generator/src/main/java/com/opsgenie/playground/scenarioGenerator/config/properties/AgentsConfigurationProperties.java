package com.opsgenie.playground.scenarioGenerator.config.properties;

import com.opsgenie.playground.scenarioGenerator.entity.Agent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@Component
@ConfigurationProperties
public class AgentsConfigurationProperties {

    private Map<String, Agent> agents = new HashMap<>();

    public Map<String, Agent> getAgents() {
        return agents;
    }

    public void setAgents(Map<String, Agent> agents) {
        this.agents = agents;
    }
}
