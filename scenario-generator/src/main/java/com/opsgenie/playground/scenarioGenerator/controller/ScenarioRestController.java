package com.opsgenie.playground.scenarioGenerator.controller;

import com.opsgenie.playground.scenarioGenerator.controller.dto.StartScenarioRequest;
import com.opsgenie.playground.scenarioGenerator.entity.ScenarioStatus;
import com.opsgenie.playground.scenarioGenerator.entity.Scenario;
import com.opsgenie.playground.scenarioGenerator.service.scenario.ScenarioRunner;
import com.opsgenie.playground.scenarioGenerator.service.scenario.ScenarioService;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
@RestController
@RequestMapping("/agents/{agentName}")
public class ScenarioRestController {

    private final ScenarioService scenarioService;

    private final ScenarioRunner scenarioRunner;

    @Autowired
    public ScenarioRestController(ScenarioService scenarioService, ScenarioRunner scenarioRunner) {
        this.scenarioService = scenarioService;
        this.scenarioRunner = scenarioRunner;
    }

    @RequestMapping("/scenarios")
    public List<Scenario> getAgentsScenarios(@NotBlank @PathVariable("agentName") String agentName) throws Exception {

        return scenarioService.getScenarios(agentName);
    }

    @RequestMapping("/scenarioStatus")
    public ScenarioStatus getAgentsScenarioStatus(@NotBlank @PathVariable("agentName") String agentName) throws Exception {

        return scenarioRunner.status(agentName);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/scenarios/{scenarioId}/start", method = {RequestMethod.POST, RequestMethod.PUT})
    public ScenarioStatus startScenario(@NotBlank @PathVariable("agentName") String agentName,
                                        @NotBlank @PathVariable("scenarioId") String scenarioId,
                                        @RequestBody StartScenarioRequest request) throws Exception {

        return scenarioRunner.start(agentName, scenarioId, request.getRequestedTime());
    }

}
