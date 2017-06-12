package com.opsgenie.playground.scenarioGenerator.service.scenario.exception;

/**
 * @author Serhat Can
 * @version 02/06/17
 */
public class ScenarioDoesNotExist extends Exception {

    public ScenarioDoesNotExist(String message) {
        super(message);
    }
}
