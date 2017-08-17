package com.opsgenie.playground.scenarioGeneratorAgent.scenario.entity;

import com.opsgenie.playground.scenarioGenerator.entity.Scenario;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
public class EasyTravelScenario extends Scenario {

    private String area;

    private String group;

    public EasyTravelScenario(String id, String area, String group) {
        super(id);
        this.area = area;
        this.group = group;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "EasyTravelScenario{" +
                "area='" + area + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
