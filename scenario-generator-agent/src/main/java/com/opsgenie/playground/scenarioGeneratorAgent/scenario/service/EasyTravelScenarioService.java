package com.opsgenie.playground.scenarioGeneratorAgent.scenario.service;

import com.opsgenie.playground.scenarioGeneratorAgent.scenario.entity.EasyTravelScenario;
import com.opsgenie.playground.scenarioGeneratorAgent.scenario.config.properties.EasyTravelConfigurationProperties;
import com.opsgenie.playground.scenarioGeneratorAgent.scenario.config.properties.ScenarioConfigProperty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * @author Serhat Can
 * @version 31/05/17
 */
public class EasyTravelScenarioService implements ScenarioService<EasyTravelScenario> {

    private static final Logger log = LoggerFactory.getLogger(EasyTravelScenarioService.class);

    private static final String CONFIGURATION_SERVICE = "/services/ConfigurationService";

    private RestTemplate restTemplate;

    private DocumentBuilderFactory documentBuilderFactory;

    private XPathFactory xPathFactory;

    private String apiUrl;

    private Map<String, ScenarioConfigProperty> availableScenarioConfigsMap;
    private List<String> availableScenarioNames;

    public EasyTravelScenarioService(RestTemplate restTemplate, EasyTravelConfigurationProperties conf) {
        this.restTemplate = restTemplate;
        this.xPathFactory = XPathFactory.newInstance();
        this.documentBuilderFactory = DocumentBuilderFactory.newInstance();

        this.apiUrl = conf.getApiUrl();
        this.availableScenarioConfigsMap = conf.getAvailableScenarios().stream().collect(Collectors.toMap(ScenarioConfigProperty::getName, sc -> sc));
        this.availableScenarioNames = conf.getAvailableScenarioNames();
    }

    @Override
    public List<EasyTravelScenario> getScenarios() throws Exception {
        List<EasyTravelScenario> allAvailableScenarios = getAllScenarios();

        List<String> enabledScenarios = getEnabledPluginNames();

        allAvailableScenarios.forEach(pl -> {

            if (enabledScenarios.contains(pl.getName())) {
                pl.setEnabled(true);
            }
        });

        return allAvailableScenarios;
    }

    private List<EasyTravelScenario> getAllScenarios() throws Exception {
        String pluginNamesString = restTemplate.getForObject(apiUrl + CONFIGURATION_SERVICE + "/getAllPlugins", String.class);
        List<EasyTravelScenario> scenarios = getScenariosFromResponse(pluginNamesString, "/getAllPluginsResponse/return/text()");

        final List<EasyTravelScenario> availableScenarios = new ArrayList<>();

        for (EasyTravelScenario scenario : scenarios) {
            if (availableScenarioConfigsMap.containsKey(scenario.getName())) {
                final ScenarioConfigProperty scenarioConfigProperty = availableScenarioConfigsMap.get(scenario.getName());
                scenario.setMaxTime(scenarioConfigProperty.getMaxTime());
                scenario.setMinTime(scenarioConfigProperty.getMinTime());

                availableScenarios.add(scenario);
            }
        }

        return availableScenarios;
    }

    private List<EasyTravelScenario> getScenariosFromResponse(String objectString, String expression) throws Exception {
        NodeList nodes = getNodeListForDocAndExpression(objectString, expression);

        List<EasyTravelScenario> scenarios = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            String nodeValue = nodes.item(i).getNodeValue();
            String[] values = nodeValue.split(":");// expected to return 4 value - no need to check
            scenarios.add(new EasyTravelScenario(values[0], values[1], values[2], values.length > 3 ? values[3] : ""));
        }
        return scenarios;
    }

    private NodeList getNodeListForDocAndExpression(String objectString, String expression) throws Exception {
        DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
        InputSource source = new InputSource(new StringReader(objectString));
        Document doc = builder.parse(source);
        XPath xpath = xPathFactory.newXPath();
        return (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);
    }

    private List<String> getEnabledPluginNames() throws Exception {
        String pluginNamesString = restTemplate.getForObject(apiUrl + CONFIGURATION_SERVICE + "/getEnabledPluginNames", String.class);
        NodeList nodes = getNodeListForDocAndExpression(pluginNamesString, "/getEnabledPluginNamesResponse/return/text()");
        List<String> pluginNames = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            pluginNames.add(nodes.item(i).getNodeValue());
        }


        return pluginNames.stream().filter(pluginName -> availableScenarioNames.contains(pluginName)).collect(Collectors.toList());
    }

    @Override
    public boolean enable(String scenarioName) throws Exception {
        return setScenarioEnabled(scenarioName, true);
    }

    @Override
    public boolean disable(String scenarioName) throws Exception {
        return setScenarioEnabled(scenarioName, false);
    }

    private boolean setScenarioEnabled(String scenarioName, boolean enabled) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl + CONFIGURATION_SERVICE + "/setPluginEnabled")
                .queryParam("name", scenarioName)
                .queryParam("enabled", enabled);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        HttpEntity<String> response = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, entity, String.class);

        if (!((ResponseEntity) response).getStatusCode().is2xxSuccessful()) {
            log.error("Response from plugin enable is not successful", response);
            throw new HttpServerErrorException(((ResponseEntity) response).getStatusCode());
        }

        return enabled;
    }

    @Override
    public void reset() throws Exception {
        log.info("Application context refresh called in EasyTravel scenario service. Should disable all available plugins now...");

        final List<EasyTravelScenario> allScenarios = getAllScenarios();

        for (EasyTravelScenario scenario : allScenarios) {
            disable(scenario.getName());
        }
    }
}
