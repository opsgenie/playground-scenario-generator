package com.opsgenie.playground.scenarioGeneratorAgent.scenario.config;

import com.opsgenie.playground.scenarioGeneratorAgent.scenario.config.properties.EasyTravelConfigurationProperties;
import com.opsgenie.playground.scenarioGeneratorAgent.scenario.service.BadgesScenarioService;
import com.opsgenie.playground.scenarioGeneratorAgent.scenario.service.EasyTravelScenarioService;
import com.opsgenie.playground.scenarioGeneratorAgent.scenario.service.ScenarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
@Configuration
public class ScenarioServiceConfig {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EasyTravelConfigurationProperties easyTravelConfigurationProperties;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnProperty(name = "scenario.application", havingValue = "easyTravel", matchIfMissing = true)
    ScenarioService easyTravelScenarioService() {
        return new EasyTravelScenarioService(restTemplate, easyTravelConfigurationProperties);
    }

    @Bean
    @ConditionalOnProperty(name = "scenario.application", havingValue = "badges")
    ScenarioService badgesScenarioService() {
        return new BadgesScenarioService();
    }
}
