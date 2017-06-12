package com.opsgenie.playground.scenarioGenerator.service.scenario;

import com.opsgenie.playground.scenarioGenerator.entity.Scenario;
import com.opsgenie.playground.scenarioGenerator.service.agent.AgentService;
import com.opsgenie.playground.scenarioGenerator.service.scenario.exception.ScenarioDoesNotExist;
import com.opsgenie.playground.scenarioGenerator.util.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@Service
public class ScenarioService {

    private static final Logger log = LoggerFactory.getLogger(ScenarioService.class);

    private final AgentService agentService;

    private final RestTemplate restTemplate;

    @Autowired
    public ScenarioService(AgentService agentService, RestTemplate restTemplate) {
        this.agentService = agentService;
        this.restTemplate = restTemplate;
    }

    public List<Scenario> getScenarios(String agentName) throws IOException {
        String scenariosString = restTemplate.getForObject(getAgentUrlFromName(agentName) + "/scenarios", String.class);
        return JSON.parseAsList(scenariosString, Scenario.class);
    }

    private String getAgentUrlFromName(String agentName) {
        return agentService.getAgent(agentName).getUrl();
    }

    /**
     * TODO serhat:
     * This service gets all scenarios and filters it. This should change later and agents should
     * have an endpoint that provides specific scenario details
     */
    public Scenario getScenario(String agentName, String scenarioName) throws IOException, ScenarioDoesNotExist {
        final List<Scenario> scenarios = getScenarios(agentName);
        final Optional<Scenario> scenarioOptional = scenarios.stream().filter(sc -> sc.getName().equalsIgnoreCase(scenarioName)).findFirst();
        if (!scenarioOptional.isPresent()) {
            throw new ScenarioDoesNotExist("Scenario '" + scenarioName + "' does not exist.");
        }

        return scenarioOptional.get();
    }

    public void enableScenario(String agentName, String scenarioName) {
        log.info("Enabling scenario '" + scenarioName + "' for '" + agentName + "' agent.");
        restTemplate.postForLocation(getAgentUrlFromName(agentName) + "/scenarios/" + scenarioName + "/enable", String.class);
    }

    public void disableScenario(String agentName, String scenarioName) {
        log.info("Disabling scenario '" + scenarioName + "' for '" + agentName + "' agent.");
        restTemplate.postForLocation(getAgentUrlFromName(agentName) + "/scenarios/" + scenarioName + "/disable", String.class);
    }

    @EventListener(ContextRefreshedEvent.class)
    void reset() {
        agentService.getAgents().forEach(agent -> {
            log.info("Requesting to reset agent [" + agent.getName() + "] scenarios.");
            restTemplate.postForLocation(agent.getUrl() + "/reset", String.class);
        });
    }
}