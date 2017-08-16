package com.opsgenie.playground.scenarioGenerator.service.scenario;

import com.opsgenie.playground.scenarioGenerator.entity.Agent;
import com.opsgenie.playground.scenarioGenerator.entity.Scenario;
import com.opsgenie.playground.scenarioGenerator.entity.ScenarioStatus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@RunWith(MockitoJUnitRunner.class)
public class OnlyOneInAllScenarioRunnerTest {

    private static final String EASY_TRAVEL = "easyTravel";
    private static final String BADGES = "badges";

    @Mock
    private ScenarioService scenarioService;

    @InjectMocks
    private OnlyOneInAllScenarioRunner runner;

    private List<Agent> agents = new ArrayList<>();

    private Scenario etScenario1;
    private Scenario etScenario2;
    private Scenario etScenario3;

    public OnlyOneInAllScenarioRunnerTest() {
        agents.add(new Agent(EASY_TRAVEL, "http://easytravel.com"));
        agents.add(new Agent(BADGES, "httzps://badges.com"));

        etScenario1 = new Scenario("scenario1","scenario1", "scenario1 desc", false, 5, 80);
        etScenario2 = new Scenario("scenario2","scenario2", "scenario2 desc", false, 10, 180);
        etScenario3 = new Scenario("scenario3","scenario3", "scenario3 desc", false, 0, 50);
    }

    @Before
    public void setup() throws Exception {
        when(scenarioService.getScenario(EASY_TRAVEL, etScenario1.getName())).thenReturn(etScenario1);
        when(scenarioService.getScenario(EASY_TRAVEL, etScenario2.getName())).thenReturn(etScenario2);
        when(scenarioService.getScenario(EASY_TRAVEL, etScenario3.getName())).thenReturn(etScenario3);

        doNothing().when(scenarioService).enableScenario(EASY_TRAVEL, etScenario1.getName());
    }

    @Test
    public void testStatus() throws Exception {
        final ScenarioStatus availableStatus = runner.status(EASY_TRAVEL);
        assertScenarioStatus(availableStatus, ScenarioStatus.AVAILABLE, 0, 0, null, false);

        final ScenarioStatus newScenario = runner.start(EASY_TRAVEL, etScenario1.getName(), 4);
        assertScenarioStatus(newScenario, ScenarioStatus.IN_PROGRESS, 5, 5, etScenario1, true);

        final ScenarioStatus runningStatus = runner.status(EASY_TRAVEL);
        assertScenarioStatus(runningStatus, ScenarioStatus.IN_PROGRESS, 5, 5, etScenario1, true);

        final ScenarioStatus busyScenario = runner.start(EASY_TRAVEL, etScenario2.getName(), 40);
        assertScenarioStatus(busyScenario, ScenarioStatus.IN_PROGRESS, 5, 5, etScenario1, true);
    }

    @Test
    public void testStartSuccessfullyWithLowRequestedTimeThanExpected() throws Exception {
        final ScenarioStatus ssWithLowerRequestedTime = runner.start(EASY_TRAVEL, etScenario1.getName(), 2);
        assertScenarioStatus(ssWithLowerRequestedTime, ScenarioStatus.IN_PROGRESS, 5, 5, etScenario1, true);
    }

    @Test
    public void testStartSuccessfullyWithRegularRequestedTime() throws Exception {
        final ScenarioStatus ssWithNormalRequestedTime = runner.start(EASY_TRAVEL, etScenario1.getName(), 10);
        assertScenarioStatus(ssWithNormalRequestedTime, ScenarioStatus.IN_PROGRESS, 10, 10, etScenario1, true);
    }

    @Test
    public void testStartSuccessfullyWithHighRequestedTimeThanExpected() throws Exception {
        final ScenarioStatus ssWithNormalRequestedTime = runner.start(EASY_TRAVEL, etScenario1.getName(), 90);
        assertScenarioStatus(ssWithNormalRequestedTime, ScenarioStatus.IN_PROGRESS, 80, 80, etScenario1, true);
    }

    @Test
    public void testCheckAndReleaseScenariosOnTime() throws Exception {
        final ScenarioStatus runningScenarioStatus = runner.start(EASY_TRAVEL, etScenario3.getName(), 0);
        assertScenarioStatus(runningScenarioStatus, ScenarioStatus.IN_PROGRESS, 0, 0, etScenario3, true);

        final ScenarioStatus running = runner.status(EASY_TRAVEL);
        assertScenarioStatus(running, ScenarioStatus.IN_PROGRESS, 0, 0, etScenario3, true);

        runner.checkAndReleaseScenariosOnTime();

        final ScenarioStatus availableStatus = runner.status(EASY_TRAVEL);
        assertScenarioStatus(availableStatus, ScenarioStatus.AVAILABLE, 0, 0, null, false);

        final ScenarioStatus runningScenarioStatus2 = runner.start(EASY_TRAVEL, etScenario3.getName(), 1);
        assertScenarioStatus(runningScenarioStatus2, ScenarioStatus.IN_PROGRESS, 1, 1, etScenario3, true);

        runner.checkAndReleaseScenariosOnTime();

        final ScenarioStatus availableStatus2 = runner.status(EASY_TRAVEL);
        assertScenarioStatus(availableStatus2, ScenarioStatus.IN_PROGRESS, 1, 1, etScenario3, true);

    }

    private void assertScenarioStatus(ScenarioStatus scenarioStatus, String status, int requestedSeconds, int remainingSeconds, Scenario scenario, boolean running) {
        assertThat(scenarioStatus.getStatus(), is(equalTo(status)));
        assertThat(scenarioStatus.getRequestedSeconds(), is(equalTo(requestedSeconds)));
        assertThat(scenarioStatus.getRemainingSeconds(), is(equalTo(remainingSeconds)));
        assertThat(scenarioStatus.getRunningScenario(), is(equalTo(scenario)));
        assertThat(scenarioStatus.isRunning(), is(equalTo(running)));
    }
}