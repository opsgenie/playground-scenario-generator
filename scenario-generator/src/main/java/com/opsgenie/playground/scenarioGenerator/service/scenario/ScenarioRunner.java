package com.opsgenie.playground.scenarioGenerator.service.scenario;

import com.opsgenie.playground.scenarioGenerator.entity.ScenarioStatus;
import com.opsgenie.playground.scenarioGenerator.service.scenario.exception.ScenarioDoesNotExist;

import java.io.IOException;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public interface ScenarioRunner {

    ScenarioStatus status(String agentName);

    ScenarioStatus start(String agentName, String scenarioName, int requestedTime) throws ScenarioDoesNotExist, IOException;

}
