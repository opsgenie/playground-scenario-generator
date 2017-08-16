package com.opsgenie.playground.scenarioGeneratorAgent.scenario.controller;

import com.opsgenie.playground.scenarioGeneratorAgent.scenario.service.ScenarioService;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/scenarios")
public class ScenarioRestController {

    private final ScenarioService scenarioService;

    @Autowired
    public ScenarioRestController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List getScenarios() throws Exception {
        return scenarioService.getScenarios();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{scenario}/enable", method = {RequestMethod.POST, RequestMethod.PUT})
    public boolean enableScenario(@NotBlank @PathVariable("scenario") String scenarioId) throws Exception {
        return scenarioService.enable(scenarioId);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{scenario}/disable", method = {RequestMethod.POST, RequestMethod.PUT})
    public boolean disableScenario(@NotBlank @PathVariable("scenario") String scenarioId) throws Exception {
        return scenarioService.disable(scenarioId);
    }

}
