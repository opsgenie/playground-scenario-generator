package com.opsgenie.playground.scenarioGenerator.service.scenario.exception;

import com.opsgenie.playground.scenarioGenerator.entity.Scenario;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public class ScenarioAlreadyInProgressException extends Exception {

    private Scenario runningScenario;

    public ScenarioAlreadyInProgressException(String message, Scenario scenario) {
        super(message);
        this.runningScenario = scenario;
    }

    public Scenario getRunningScenario() {
        return runningScenario;
    }
}
