package com.opsgenie.playground.scenarioGeneratorAgent.scenario.service;

import com.opsgenie.playground.scenarioGeneratorAgent.scenario.entity.BadgesScenario;

import java.util.List;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
public class BadgesScenarioService implements ScenarioService<BadgesScenario> {

    @Override
    public List<BadgesScenario> getScenarios() {
        return null;
    }

    @Override
    public boolean enable(String scenarioName) {
        return false;
    }

    @Override
    public boolean disable(String scenarioName) {
        return false;
    }

    @Override
    public void reset() {

    }
}
