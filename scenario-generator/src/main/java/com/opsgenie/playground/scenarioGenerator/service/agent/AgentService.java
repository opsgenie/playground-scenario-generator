package com.opsgenie.playground.scenarioGenerator.service.agent;

import com.opsgenie.playground.scenarioGenerator.entity.Agent;

import java.util.List;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public interface AgentService {

    List<Agent> getAgents();

    Agent getAgent(String agentName);
}
