package com.opsgenie.playground.scenarioGenerator.controller;

import com.opsgenie.playground.scenarioGenerator.service.agent.AgentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@RestController
@RequestMapping("/agents")
public class AgentRestController {

    private final AgentService agentService;

    @Autowired
    public AgentRestController(AgentService agentService) {
        this.agentService = agentService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List getAgents() {
        return agentService.getAgents();
    }
}
