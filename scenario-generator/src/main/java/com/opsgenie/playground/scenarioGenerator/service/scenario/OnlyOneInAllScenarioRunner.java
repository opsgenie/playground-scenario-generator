package com.opsgenie.playground.scenarioGenerator.service.scenario;

import com.opsgenie.playground.scenarioGenerator.entity.ScenarioStatus;
import com.opsgenie.playground.scenarioGenerator.entity.Scenario;

import com.opsgenie.playground.scenarioGenerator.service.scenario.exception.ScenarioDoesNotExist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.toIntExact;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
@Service
public class OnlyOneInAllScenarioRunner implements ScenarioRunner {

    private static final Logger log = LoggerFactory.getLogger(OnlyOneInAllScenarioRunner.class);

    private static final int SCENARIO_CHECK_INTERVAL = 1000;

    private Map<String, ScenarioWrapper> runningScenariosMap = new HashMap<>();

    private final ScenarioService scenarioService;

    @Autowired
    public OnlyOneInAllScenarioRunner(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @Override
    public ScenarioStatus status(String agentName) {
        if (runningScenariosMap.containsKey(agentName)) {
            ScenarioWrapper running = runningScenariosMap.get(agentName);
            return ScenarioStatus.createInProgressStatus(running.getScenario() , running.getRemainingSeconds(), running.getRequestedSeconds());
        } else {
            return ScenarioStatus.createAvailableStatus();
        }
    }

    @Scheduled(fixedRate = SCENARIO_CHECK_INTERVAL)
    public void checkAndReleaseScenariosOnTime() {

        for(Map.Entry<String, ScenarioWrapper> entry: runningScenariosMap.entrySet()) {
            String agentName = entry.getKey();
            ScenarioWrapper scw = entry.getValue();
            if (scw.getRemainingSeconds() <= 0) {
                scenarioService.disableScenario(agentName, scw.getScenario().getId());
                runningScenariosMap.remove(agentName);
                log.info("Scenario '" + scw.getScenario().getName() + "' is finished. " + scw.toString());
            }
        }
    }

    @Override
    public ScenarioStatus start(String agentName, String scenarioId, int requestedTime) throws ScenarioDoesNotExist, IOException {
        final Scenario scenario = scenarioService.getScenario(agentName, scenarioId);

        if (runningScenariosMap.containsKey(agentName)) {
            ScenarioWrapper running = runningScenariosMap.get(agentName);
            return ScenarioStatus.createInProgressStatus(running.getScenario() , running.getRemainingSeconds(), running.getRequestedSeconds());
        }

        scenarioService.enableScenario(agentName, scenarioId);

        final int finalRequestedTime = validateAndGetRequestedTime(scenario, requestedTime);
        final long currentTimeInSeconds = getCurrentTimeInSec();

        ScenarioWrapper running = createNewScenarioWrapper(scenario, finalRequestedTime, currentTimeInSeconds);

        runningScenariosMap.put(agentName, running);

        return ScenarioStatus.createInProgressStatus(running.getScenario() , running.getRemainingSeconds(), running.getRequestedSeconds());
    }

    private ScenarioWrapper createNewScenarioWrapper(Scenario scenario, int requestedTime, long currentTimeInSec) {
        ScenarioWrapper scenarioWrapper = new ScenarioWrapper();
        scenarioWrapper.setScenario(scenario);
        scenarioWrapper.setRequestedSeconds(requestedTime);
        scenarioWrapper.setFinishTime(currentTimeInSec + requestedTime);
        scenarioWrapper.setStartTime(currentTimeInSec);
        return scenarioWrapper;
    }

    private int validateAndGetRequestedTime(Scenario scenario, int requestedTime) {
        if(scenario.getMinTime() >= requestedTime) {
            return scenario.getMinTime();
        } else if (requestedTime >= scenario.getMaxTime()) {
            return scenario.getMaxTime();
        } else {
            return requestedTime;
        }
    }

    private static long getCurrentTimeInSec() {
        return System.currentTimeMillis() / 1000;
    }

    static class ScenarioWrapper {
        private Scenario scenario;
        private int requestedSeconds;
        private long startTime;
        private long finishTime;

        public Scenario getScenario() {
            return scenario;
        }

        public void setScenario(Scenario scenario) {
            this.scenario = scenario;
        }

        public int getRequestedSeconds() {
            return requestedSeconds;
        }

        public void setRequestedSeconds(int requestedSeconds) {
            this.requestedSeconds = requestedSeconds;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }

        /**
         * Important!
         */
        public int getRemainingSeconds() {
            return toIntExact(finishTime - getCurrentTimeInSec());
        }

        @Override
        public String toString() {
            return "ScenarioWrapper{" +
                    "scenario=" + scenario +
                    ", requestedSeconds=" + requestedSeconds +
                    ", startTime=" + startTime +
                    ", finishTime=" + finishTime +
                    '}';
        }
    }
}
