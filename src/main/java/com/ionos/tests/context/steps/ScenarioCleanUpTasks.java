package com.ionos.tests.context.steps;

import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import io.jtest.utils.clients.http.HttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ScenarioCleanUpTasks {
  @Inject ScenarioVars scenarioVars;

  public void addCleanupTask(Runnable task) {
    getTasksListInScenarioProps().add(task);
  }

  public void addCleanupTask(HttpClient httpCall) {
    getTasksListInScenarioProps()
        .add(
            () -> {
              try {
                httpCall.execute();
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            });
  }

  public void runCleanupTasks() {
    for (Runnable task : getTasksListInScenarioProps()) {
      try {
        task.run();
      } catch (Exception e) {
        log.error("Error: Cleanup Task can not be completed");
      }
    }
  }

  private List<Runnable> getTasksListInScenarioProps() {
    Object cleanupTasks = scenarioVars.get("cleanupTasks");
    if (cleanupTasks == null) {
      scenarioVars.put("cleanupTasks", new ArrayList<Runnable>());
      cleanupTasks = scenarioVars.get("cleanupTasks");
    }
    return (List<Runnable>) cleanupTasks;
  }

  protected Logger log = LogManager.getLogger();
}
