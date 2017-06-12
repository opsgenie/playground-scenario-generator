package com.opsgenie.playground.scenarioGeneratorAgent.scenario.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
@Component
@ConfigurationProperties("easyTravel")
public class EasyTravelConfigurationProperties {

    private String apiUrl;

    private List<ScenarioConfigProperty> availableScenarios = new ArrayList<>();

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public List<ScenarioConfigProperty> getAvailableScenarios() {
        return availableScenarios;
    }

    public void setAvailableScenarios(List<ScenarioConfigProperty> availableScenarios) {
        this.availableScenarios = availableScenarios;
    }

    public List<String> getAvailableScenarioNames() {
        return availableScenarios.stream().map(ScenarioConfigProperty::getName).collect(Collectors.toList());
    }
}
