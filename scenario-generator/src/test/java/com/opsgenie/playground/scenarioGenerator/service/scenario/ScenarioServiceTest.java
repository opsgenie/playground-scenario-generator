package com.opsgenie.playground.scenarioGenerator.service.scenario;

import com.opsgenie.playground.scenarioGenerator.entity.Agent;
import com.opsgenie.playground.scenarioGenerator.entity.Scenario;
import com.opsgenie.playground.scenarioGenerator.service.agent.AgentService;
import com.opsgenie.playground.scenarioGenerator.service.scenario.exception.ScenarioDoesNotExist;
import com.opsgenie.playground.scenarioGenerator.util.JSON;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Serhat Can
 * @version 07/06/17
 */
@RunWith(MockitoJUnitRunner.class)
public class ScenarioServiceTest {

    private static final String EASY_TRAVEL = "easyTravel";

    private Agent agent;

    private Scenario scenario1;

    private Scenario scenario2;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AgentService agentService;

    @InjectMocks
    private ScenarioService scenarioService;

    public ScenarioServiceTest() {
        this.agent = new Agent(EASY_TRAVEL, "http://a.com", "Travel fast and easy");
        this.scenario1 = new Scenario("sce1","sce1", "senaryo 1", true, 30, 50);
        this.scenario2 = new Scenario("sce2","sce2", "senaryo 2");
    }

    @Before
    public void setUp() throws Exception {
        when(agentService.getAgent(EASY_TRAVEL)).thenReturn(agent);

        final List<Scenario> scenarios = Arrays.asList(scenario1, scenario2);
        when(restTemplate.getForObject("http://a.com/scenarios", String.class)).thenReturn(JSON.toJson(scenarios));
    }

    @Test
    public void testGetScenarios() throws Exception {
        final List<Scenario> returnedScenarios = scenarioService.getScenarios(EASY_TRAVEL);

        verify(restTemplate, times(1)).getForObject("http://a.com/scenarios", String.class);
        assertThat(Arrays.asList(scenario1, scenario2), is(equalTo(returnedScenarios)));
    }

    @Test
    public void testGetScenario() throws Exception {
        final List<Scenario> scenarios = Arrays.asList(scenario1, scenario2);
        when(restTemplate.getForObject("http://a.com/scenarios", String.class)).thenReturn(JSON.toJson(scenarios));

        final Scenario sce1 = scenarioService.getScenario(EASY_TRAVEL, "sce1");
        assertThat(sce1, is(equalTo(scenario1)));

        final Scenario sce2 = scenarioService.getScenario(EASY_TRAVEL, "sce2");
        assertThat(sce2, is(equalTo(scenario2)));
    }

    @Test(expected = ScenarioDoesNotExist.class)
    public void testGetScenarioThrowsScenarioDoesNotExistException() throws Exception {
        final List<Scenario> scenarios = Arrays.asList(scenario1);
        when(restTemplate.getForObject("http://a.com/scenarios", String.class)).thenReturn(JSON.toJson(scenarios));

        scenarioService.getScenario(EASY_TRAVEL, "sce2");
    }

    @Test
    public void testEnableScenario() throws Exception {
        scenarioService.enableScenario(EASY_TRAVEL, "scenario1");
        verify(restTemplate, times(1)).postForLocation("http://a.com/scenarios/scenario1/enable", String.class);
    }

    @Test
    public void testDisableScenario() throws Exception {
        scenarioService.disableScenario(EASY_TRAVEL, "scenario1");
        verify(restTemplate, times(1)).postForLocation("http://a.com/scenarios/scenario1/disable", String.class);
    }

}