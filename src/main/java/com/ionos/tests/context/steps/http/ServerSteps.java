package com.ionos.tests.context.steps.http;

import com.cucumber.utils.context.ScenarioVarsUtils;
import com.cucumber.utils.context.vars.ScenarioVars;
import com.google.inject.Inject;
import com.ionos.tests.config.PropertiesConfig;
import com.ionos.tests.context.steps.RestScenario;
import com.ionos.tests.service.http.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.jtest.utils.common.StringFormat;
import java.util.Map;

public class ServerSteps extends RestScenario {
  @Inject private ServerService serverService;

  private static String URL = PropertiesConfig.API_URL;

  @Inject private RequestService requestService;

  @Inject
  public ServerSteps(ScenarioVars scenarioVars) {
    ScenarioVarsUtils.loadScenarioVarsFromFile("templates/servers.yaml", scenarioVars);
  }

  @Given("User is authenticated successfully with username = {} and password = {}")
  public void authenticationIsSuccessfulWithAValidUsernameAndValidPassword(
      String username, String password) {

    String requestBody =
        "username="
            + username
            + "&"
            + "password="
            + password
            + "&"
            + "grant_type="
            + PropertiesConfig.GRANT_TYPE
            + "&"
            + "client_id="
            + PropertiesConfig.CLIENT_ID
            + "&"
            + "client_secret="
            + PropertiesConfig.CLIENT_SECRET;

    executeAndCompare(
        serverService.login(PropertiesConfig.SERVER_AUTH_URL, keyloackLoginHeaders(), requestBody),
        scenarioVars.getAsString("authenticationResponse"));
    URL = PropertiesConfig.API_URL;
  }

  @Given("Create server with name={}, cores={} ,ram={}, storage={} and check response")
  public void createServerWithIdNameCoresRamStorageAndCheckResponse(
      String name, String cores, String ram, String storage) {
    String requestBodyPost =
        StringFormat.replaceProps(
            scenarioVars.getAsString("createServerRequestTemplate"),
            Map.of("name", name, "cores", cores, "ram", ram, "storage", storage));

    String expectedResponse =
        StringFormat.replaceProps(
            scenarioVars.getAsString("createServerResponseTemplate"),
            Map.of("name", name, "cores", cores, "ram", ram, "storage", storage));

    executeAndCompare(
        serverService.prepareCreate(URL, authorizationHeader(), requestBodyPost), expectedResponse);
  }

  @Given("Retrieve the server with id={}")
  public void retrieveTheServerWithId(String serverId) {
    executeAndCompare(
        serverService.prepareGetById(URL, authorizationHeader(), serverId),
        scenarioVars.getAsString("getServerResponseTemplate"));
  }

  @Given("Delete the server with the id={}")
  public void deleteTheServerWithTheId(String serverID) {
    executeAndCompare(
        serverService.prepareDeleteById(URL, authorizationHeader(), serverID),
        scenarioVars.getAsString("deleteServerResponse"));
  }

  @Given("Find the server with id={} and check the server is no longer available")
  public void retrieveTheServerWithIdAndCheckTheResponse(String serverId) {
    executeAndCompare(
        serverService.prepareGetById(URL, authorizationHeader(), serverId), "{\"status\":404}");
  }

  @Then(
      "Create server with id={}, name={string} cores={} ,ram={}, storage={} and  check response is {}")
  public void createServerWithIdNameCoresRamStorageAndCheckResponseIs(
      String serverId, String name, String cores, String ram, String storage, String errorMesage) {
    String requestBodyPost =
        StringFormat.replaceProps(
            scenarioVars.getAsString("createServerRequestTemplate"),
            Map.of(
                "serverId",
                serverId,
                "name",
                name,
                "cores",
                cores,
                "ram",
                ram,
                "storage",
                storage));
    executeAndCompare(
        serverService.prepareCreate(URL, authorizationHeader(), requestBodyPost), errorMesage);
  }

  @And("Retrieve  server with id={} and  check response is {}")
  public void retrieveServerWithIdAndCheckResponseIs(String serverId, String response) {
    executeAndCompare(serverService.prepareGetById(URL, authorizationHeader(), serverId), response);
  }

  @When("Updates the server with id={} with another server and check response body")
  public void updatesTheEntireServerWithAnotherServer(String serverId) {
    String requestBody = scenarioVars.getAsString("putDefaultServerRequestTemplate");
    executeAndCompare(
        serverService.updateServerById(URL, authorizationHeader(), requestBody, serverId),
        scenarioVars.getAsString("putServerResponseTemplate"));
  }

  @When("Create server with name={}, cores={} ,ram={}, storage={} and check access is forbidden")
  public void createServerAndCheckPermissionIsForbidden(
      String name, String cores, String ram, String storage) {
    String requestBodyPost =
        StringFormat.replaceProps(
            scenarioVars.getAsString("createServerRequestTemplate"),
            Map.of("name", name, "cores", cores, "ram", ram, "storage", storage));

    executeAndCompare(
        serverService.prepareCreate(URL, authorizationHeader(), requestBodyPost),
        "{\"status\":403}");
  }

  @Then("Find server with id={} and check access is forbidden")
  public void retrieveServerWithIdAndCheckResponseIs(String serverId) {
    executeAndCompare(
        serverService.prepareGetById(URL, authorizationHeader(), serverId), "{\"status\":404}");
  }

  @When("Check if create request has successfully completed")
  public void checkCreateRequestIsCompletedSuccessfully() {
    executeAndCompare(
        requestService.prepareGetById(
            URL, authorizationHeader(), scenarioVars.getAsString("requestId")),
        scenarioVars.getAsString("getCreateRequestResponseTemplate"),
        300);
  }

  @When("Check if update request has successfully completed")
  public void checkUpdateRequestIsCompletedSuccessfully() {
    executeAndCompare(
        requestService.prepareGetById(
            URL, authorizationHeader(), scenarioVars.getAsString("requestId")),
        scenarioVars.getAsString("getUpdateRequestResponseTemplate"),
        300);
  }

  @When("Check if delete request has successfully completed")
  public void checkDeleteRequestIsCompletedSuccessfully() {
    executeAndCompare(
        requestService.prepareGetById(
            URL, authorizationHeader(), scenarioVars.getAsString("requestId")),
        scenarioVars.getAsString("getDeleteRequestResponseTemplate"),
        300);
  }

  @Then("Updates the server with id={} with another server and check access is forbidden")
  public void updateServerWithIdAndCheckResponseIs(String serverId) {
    String requestBody = scenarioVars.getAsString("putDefaultServerRequestTemplate");
    executeAndCompare(
        serverService.updateServerById(URL, authorizationHeader(), requestBody, serverId),
        "{\"status\":404}");
  }

  @And("Delete  server with the id={} and check access is forbidden")
  public void deleteTheServerWithTheIdAndCheckAccessIsForbidden(String serverID) {
    executeAndCompare(
        serverService.prepareDeleteById(URL, authorizationHeader(), serverID), "{\"status\":404}");
  }

  @Given("Access token is invalid")
  public void accessTokenIsInvalid() {
    scenarioVars.put("access_token", "access_token");
  }

  @Then("Retrieve all Servers and  check response is {}")
  public void retrieveAllServersAndCheckResponseIs(String responseBody) {
    executeAndCompare(serverService.retrieveAllServers(URL, authorizationHeader()), responseBody);
  }

  @Then("Update server with id={} and check response is {}")
  public void updateServerWithIdAndCheckResponseIsForbidden(String serverId, String response) {
    String requestBody = scenarioVars.getAsString("putDefaultServerRequestTemplate");
    executeAndCompare(
        serverService.updateServerById(URL, authorizationHeader(), requestBody, serverId),
        response);
  }

  @Then("Delete server with id={} and check response is {}")
  public void deleteServerWithIdServerIdAndCheckResponseIs(String serverId, String response) {
    executeAndCompare(
        serverService.prepareDeleteById(URL, authorizationHeader(), serverId), response);
  }

  public void deleteServerAuthenticatedAsAdmin(String serverId) {
    authenticationIsSuccessfulWithAValidUsernameAndValidPassword("admin", "admin");
    deleteTheServerWithTheId(serverId);
  }
}
