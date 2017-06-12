package com.opsgenie.playground.scenarioGeneratorAgent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
@EnableAutoConfiguration
@SpringBootApplication
public class ScenarioGeneratorAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScenarioGeneratorAgentApplication.class, args);
    }

}
