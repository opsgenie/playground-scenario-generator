package com.opsgenie.playground.scenarioGeneratorAgent.scenario.service;

import com.opsgenie.playground.scenarioGenerator.entity.Scenario;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * The interface Scenario service.
 *
 * @param <T> the type parameter
 * @author Serhat Can
 * @version 31 /05/17
 */
public interface ScenarioService<T extends Scenario> {


    /**
     * Gets all available scenarios
     *
     * @return the scenarios
     * @throws Exception the exception
     */
    List<T> getScenarios() throws Exception;

    /**
     * Enable a scenario
     *
     * @param scenarioName the scenario name
     * @return the boolean
     * @throws Exception the exception
     */
    boolean enable(String scenarioName) throws Exception;

    /**
     * Disable a scenario
     *
     * @param scenarioName the scenario name
     * @return the boolean
     * @throws Exception the exception
     */
    boolean disable(String scenarioName) throws Exception;

    /**
     * As we currently do not keep state in a remote storage, we will need to reset/disable all the
     * enabled scenarios if exists on application context refresh
     *
     * @throws Exception the exception
     */
    @EventListener(ContextRefreshedEvent.class)
    void reset() throws Exception;

}
