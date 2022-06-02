package com.ionos.tests.context.steps;

import com.cucumber.utils.context.ScenarioUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class BaseScenario {
  @Inject protected ScenarioUtils scenarioUtils;
  @Inject protected ScenarioVars scenarioVars;
  @Inject public static ObjectMapper mapper = new ObjectMapper();
  @Inject protected ScenarioCleanUpTasks scenarioCleanUpTasks;
}
