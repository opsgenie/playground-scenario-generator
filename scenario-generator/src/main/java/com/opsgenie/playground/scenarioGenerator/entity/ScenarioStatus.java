package com.opsgenie.playground.scenarioGenerator.entity;

/**
 * @author Serhat Can
 * @version 01/06/17
 */
public class ScenarioStatus {

    public static final String AVAILABLE = "Available";
    public static final String IN_PROGRESS = "In progress";

    private boolean running = false;

    private String status = "";

    private Scenario runningScenario;

    private int remainingSeconds;

    private int requestedSeconds;

    public ScenarioStatus() {
    }

    public static ScenarioStatus createAvailableStatus() {
        return new ScenarioStatus(AVAILABLE);
    }

    public static ScenarioStatus createInProgressStatus(Scenario runningScenario, int remainingSeconds, int requestedSeconds) {
        return new ScenarioStatus(true, IN_PROGRESS,runningScenario, remainingSeconds, requestedSeconds);
    }

    private ScenarioStatus(String status) {
        this.status = status;
    }

    private ScenarioStatus(boolean running, String status, Scenario runningScenario, int remainingSeconds, int requestedSeconds) {
        this.running = running;
        this.status = status;
        this.runningScenario = runningScenario;
        this.remainingSeconds = remainingSeconds;
        this.requestedSeconds = requestedSeconds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Scenario getRunningScenario() {
        return runningScenario;
    }

    public void setRunningScenario(Scenario runningScenario) {
        this.runningScenario = runningScenario;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getRequestedSeconds() {
        return requestedSeconds;
    }

    public ScenarioStatus setRequestedSeconds(int requestedSeconds) {
        this.requestedSeconds = requestedSeconds;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScenarioStatus that = (ScenarioStatus) o;

        if (running != that.running) return false;
        if (remainingSeconds != that.remainingSeconds) return false;
        if (requestedSeconds != that.requestedSeconds) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        return runningScenario != null ? runningScenario.equals(that.runningScenario) : that.runningScenario == null;
    }

    @Override
    public int hashCode() {
        int result = (running ? 1 : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (runningScenario != null ? runningScenario.hashCode() : 0);
        result = 31 * result + remainingSeconds;
        result = 31 * result + requestedSeconds;
        return result;
    }

    @Override
    public String toString() {
        return "ScenarioStatus{" +
                "running=" + running +
                ", status='" + status + '\'' +
                ", runningScenario=" + runningScenario +
                ", remainingSeconds=" + remainingSeconds +
                ", requestedSeconds=" + requestedSeconds +
                '}';
    }
}
