package com.opsgenie.playground.scenarioGeneratorAgent.scenario.controller;

import com.opsgenie.playground.scenarioGeneratorAgent.scenario.service.ScenarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @author Serhat Can
 * @version 08/06/17
 */
@RestController
public class AgentRestController {

    private final ScenarioService scenarioService;

    @Autowired
    public AgentRestController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/reset", method = {RequestMethod.POST, RequestMethod.PUT})
    public void disableScenario() throws Exception {
        scenarioService.reset();
    }
}
