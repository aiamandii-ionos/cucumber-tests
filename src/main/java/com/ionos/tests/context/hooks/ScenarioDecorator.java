package com.ionos.tests.context.hooks;

import com.google.inject.Inject;
import com.ionos.tests.config.PropertiesConfig;
import com.ionos.tests.context.steps.BaseScenario;
import com.ionos.tests.context.steps.http.ServerSteps;
import io.cucumber.java.*;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScenarioDecorator extends BaseScenario {

  @Inject private ServerSteps serverSteps;

  @Before(order = Integer.MAX_VALUE)
  public void fillScenarioProps() {
    scenarioVars.putAll((Map) PropertiesConfig.properties);
  }

  @After("not @ignoreAfter")
  public void deleteServer() {
    System.out.println("::group::Cleanup Tasks after test runs");
    boolean verifyCleanup =
        System.getProperties().get("verifyCleanup") == null
            || System.getProperties().get("verifyCleanup").toString().equals("true");

    if (System.getProperties().get("cleanup") == null
        || System.getProperties().get("cleanup").toString().equals("true")) {

      if (scenarioVars.getAsString("serverId") != null) {
        if (!scenarioVars.getAsString("serverId").equals("null")) {
          serverSteps.deleteServerAuthenticatedAsAdmin(scenarioVars.getAsString("serverId"));
          serverSteps.checkDeleteRequestIsCompletedSuccessfully();
        }
      }

    } else if (System.getProperties().get("cleanup").toString().equals("")) {
      System.out.println("Datacenter and IP Block will not be deleted");
    }
    System.out.println("::endgroup::");
  }

  protected Logger log = LogManager.getLogger();

  @After("not @ignoreAfter")
  public void runCleanupTasks() {
    log.info("Running Cleanup Tasks:::...");
    scenarioCleanUpTasks.runCleanupTasks();
  }
}
