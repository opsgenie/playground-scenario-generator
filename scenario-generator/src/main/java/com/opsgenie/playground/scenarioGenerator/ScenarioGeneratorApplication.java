package com.opsgenie.playground.scenarioGenerator;

import com.opsgenie.playground.scenarioGenerator.exception.RestTemplateErrorHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
@EnableAutoConfiguration
@SpringBootApplication
@EnableScheduling
public class ScenarioGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScenarioGeneratorApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(new RestTemplateErrorHandler()).build();
    }

}
