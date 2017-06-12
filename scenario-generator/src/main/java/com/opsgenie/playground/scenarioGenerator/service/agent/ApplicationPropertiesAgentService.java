package com.opsgenie.playground.scenarioGenerator.service.agent;

import com.opsgenie.playground.scenarioGenerator.config.properties.AgentsConfigurationProperties;
import com.opsgenie.playground.scenarioGenerator.entity.Agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@Service
public class ApplicationPropertiesAgentService implements AgentService {

    private final Map<String, Agent> agentMap;
    private final List<Agent> agentList;

    @Autowired
    public ApplicationPropertiesAgentService(AgentsConfigurationProperties agentsConfigurationProperties) {
        agentMap = agentsConfigurationProperties.getAgents();
        agentList = agentMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public List<Agent> getAgents() {
        return agentList;
    }

    @Override
    public Agent getAgent(String agentName) {
        return agentMap.get(agentName);
    }
}
