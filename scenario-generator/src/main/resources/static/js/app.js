angular.module('scenarioGenerator', ['ngAnimate', 'ngSanitize', 'ui.bootstrap']);

angular.module('scenarioGenerator').factory('scenarioService', function ($http) {

    var scenarioService = {};

    scenarioService.getAgents = function () {
        return $http.get('/agents');
    };

    scenarioService.getAgentStatus = function (agentName) {
        return $http.get('/agents/' + agentName + '/scenarioStatus');
    };

    scenarioService.getScenarios = function (agentName) {
        return $http.get('/agents/' + agentName + '/scenarios');
    };

    scenarioService.startScenario = function (agentName, scenarioName, payload) {
        return $http.post('/agents/' + agentName + '/scenarios/' + scenarioName + '/start', payload);
    };

    return scenarioService;
});

angular.module('scenarioGenerator').controller('scenarioCtrl', function ($scope, $http, $uibModal, $log, $document, $interval, scenarioService) {

    var $ctrl = this;

    // TODO: implement proper error handling and avoid duplicates
    var handleErrorResponse = function (response) {
        $log.error("Error response:", response);
        $ctrl.error = response.data;
        $ctrl.error.status = response.status;
    };

    var getAndSetScenarioStatusOfAnAgent = function (agent) {
        scenarioService.getAgentStatus(agent.name)
            .then(function success(response) {
                agent.scenarioStatus = response.data;
                agent.scenarioStatus.increasingSeconds = agent.scenarioStatus.requestedSeconds - agent.scenarioStatus.remainingSeconds;

                $log.debug("Scenario status: ", agent.scenarioStatus);

                //register timer for count down view on ui
                if (agent.scenarioStatus.running) {
                    var stop = $interval(function () {
                        if (agent.scenarioStatus.remainingSeconds <= 0) {
                            $interval.cancel(stop);
                            agent.scenarioStatus.running = false;
                        } else {
                            agent.scenarioStatus.remainingSeconds--;
                        }

                    }, 1000);

                    // for progress bar
                    $interval(function () {
                        agent.scenarioStatus.increasingSeconds = agent.scenarioStatus.increasingSeconds + 0.05;
                    }, 50, agent.scenarioStatus.requestedSeconds * 20);
                }

            }, handleErrorResponse);
    };

    var init = function () {
        scenarioService.getAgents()
            .then(function success(response) {
                $ctrl.agents = response.data;

                $ctrl.agents.forEach(function (agent) {
                    // TODO: get this with one request later
                    getAndSetScenarioStatusOfAnAgent(agent);
                })

            }, handleErrorResponse);
    };

    init();

    $ctrl.openStartScenarioModal = function (selectedAgent, parentSelector) {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'modal.html',
            controller: 'StartScenarioModalCtrl',
            controllerAs: '$mdl',
            resolve: {
                agent: function () {
                    return selectedAgent;
                }
            }
        });

        modalInstance.result.then(function (modalsAgent) {

            $ctrl.agents.forEach(function (agent) {
                if(agent.name === modalsAgent.name) {
                    getAndSetScenarioStatusOfAnAgent(agent);
                }
            });
        }, function () {
            $log.debug("Removing selected scenario if exists");
            $ctrl.agents.forEach(function (sc) { sc.selectedScenario = null; })
        });
    };

});

angular.module('scenarioGenerator').controller('StartScenarioModalCtrl', function ($uibModalInstance, $log, agent, scenarioService) {
    var $mdl = this;

    $mdl.agent = agent;

    var handleErrorResponse = function (response) {
        $log.error("Error response on modal:", response);
        $mdl.error = response.data;
        $mdl.error.status = response.status;
    };

    var setFormValidationMessage = function (valid, state, message) {
        $mdl.scenarioStartForm = {
            valid: valid,
            state: state,
            message: message
        };
    };

    var init = function () {
        setFormValidationMessage(false, 'success', 'Looks good!');

        scenarioService.getScenarios($mdl.agent.name)
            .then(function success(response) {
                $mdl.agent.scenarios = [];
                response.data.forEach(function (scenario) {
                    scenario.requestedSeconds = scenario.minTime;
                    $mdl.agent.scenarios.push(scenario);
                });
            }, handleErrorResponse)
    };

    init();

    $mdl.validateRequestedSeconds = function (scenario) {
        if (scenario.requestedSeconds >= scenario.minTime || scenario.requestedSeconds <= scenario.maxTime) {
            setFormValidationMessage(true, 'success', 'Looks good!');
        } else {
            setFormValidationMessage(false, 'danger',
                'Please choose a number between ' + scenario.minTime + ' and ' + scenario.maxTime);
        }
    };

    $mdl.ok = function (agent, selectedScenario) {
        if (!$mdl.scenarioStartForm.valid) return;

        scenarioService.startScenario(agent.name, selectedScenario.name, selectedScenario.requestedSeconds)
            .then(function success(response) {
                $log.debug("Scenario starting...", response.data);
                $uibModalInstance.close(agent);
            }, handleErrorResponse);
    };

    $mdl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});