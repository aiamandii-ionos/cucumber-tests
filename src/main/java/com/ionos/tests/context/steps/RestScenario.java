package com.ionos.tests.context.steps;

import com.ionos.tests.config.PropertiesConfig;
import io.cucumber.guice.ScenarioScoped;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.common.JsonUtils;
import io.jtest.utils.matcher.ObjectMatcher;
import io.jtest.utils.matcher.condition.MatchCondition;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ScenarioScoped
public class RestScenario extends BaseScenario {
  protected Logger log = LogManager.getLogger();

  public String executeAndCompare(HttpClient.Builder builder, String expected) {
    return executeAndCompare(builder, expected, null);
  }

  public String executeAndCompare(
      HttpClient.Builder builder, String expected, Integer pollDurationInSeconds) {
    return executeAndCompare(builder, expected, pollDurationInSeconds, 3000);
  }

  public String executeAndCompare(
      HttpClient.Builder builder,
      String expected,
      Integer pollDurationInSeconds,
      long retryIntervalMillis) {
    return executeAndCompare(builder, expected, pollDurationInSeconds, retryIntervalMillis, 1.0);
  }

  public String executeAndCompare(
      HttpClient.Builder builder,
      String expected,
      Integer pollDurationInSeconds,
      long retryIntervalMillis,
      double exponentialBackOff) {
    HttpClient client = builder.build();
    logRequest(client);
    final AtomicReference<CloseableHttpResponse> responseWrapper = new AtomicReference<>();
    String responseBody = "";
    try {
      if (pollDurationInSeconds == null || pollDurationInSeconds == 0) {
        responseWrapper.set(client.execute());
        scenarioVars.putAll(ObjectMatcher.matchHttpResponse(null, expected, responseWrapper.get()));
      } else if (!JsonUtils.prettyPrint(responseBody).contains("\"status\" : \"FAILED\"")) {
        scenarioVars.putAll(
            ObjectMatcher.matchHttpResponse(
                null,
                expected,
                () -> {
                  try {
                    responseWrapper.set(client.execute());
                    return responseWrapper.get();
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                },
                Duration.ofSeconds(pollDurationInSeconds),
                retryIntervalMillis,
                exponentialBackOff));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      scenarioUtils.log("----------- Comparison -----------");
      scenarioUtils.log("EXPECTED Response:\n{}", expected);
      scenarioUtils.log("--------------- vs ---------------");
      responseBody = logAndGetResponse(responseWrapper.get());
    }
    return responseBody;
  }

  public String negativeExecuteAndCompare(
      HttpClient.Builder builder, String expected, Integer pollDurationInSeconds) {
    HttpClient client = builder.build();
    logRequest(client);
    final AtomicReference<CloseableHttpResponse> responseWrapper = new AtomicReference<>();
    String responseBody = "";
    try {
      if (pollDurationInSeconds == null || pollDurationInSeconds == 0) {
        responseWrapper.set(client.execute());
        scenarioVars.putAll(
            ObjectMatcher.matchHttpResponse(
                null,
                expected,
                responseWrapper.get(),
                MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY));
      } else if (!JsonUtils.prettyPrint(responseBody).contains("\"status\" : \"FAILED\"")) {
        scenarioVars.putAll(
            ObjectMatcher.matchHttpResponse(
                null,
                expected,
                () -> {
                  try {
                    responseWrapper.set(client.execute());
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  }
                  return responseWrapper.get();
                },
                Duration.ofSeconds(pollDurationInSeconds),
                3000L,
                1.0,
                MatchCondition.DO_NOT_MATCH_HTTP_RESPONSE_BY_BODY));
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      scenarioUtils.log("----------- Comparison -----------");
      scenarioUtils.log("EXPECTED Response:\n{}", expected);
      scenarioUtils.log("--------------- vs ---------------");
      responseBody = logAndGetResponse(responseWrapper.get());
    }
    return responseBody;
  }

  private void logRequest(HttpClient client) {
    scenarioUtils.log("--------- API call details ---------");
    scenarioUtils.log("REQUEST: {}", client.getMethod() + " " + client.getUri());
    scenarioUtils.log("REQUEST HEADERS: {}", client.getHeaders());
    if (client.getRequestEntity() != null) {
      scenarioUtils.log("REQUEST BODY:\n{}", client.getRequestEntity());
    }
  }

  private String logAndGetResponse(CloseableHttpResponse actual) {
    HttpEntity entity = null;
    String responseBody = null;
    try {
      if (actual != null) {
        scenarioUtils.log("ACTUAL Response status: {}", actual.getStatusLine().getStatusCode());
        entity = actual.getEntity();
        responseBody = (entity != null) ? EntityUtils.toString(entity) : null;
        scenarioUtils.log(
            "ACTUAL Response body:\n{}",
            (responseBody != null) ? JsonUtils.prettyPrint(responseBody) : "Empty data <∅>");
        scenarioUtils.log(
            "ACTUAL Response headers: {}", Arrays.asList(actual.getAllHeaders()).toString());
      }
    } catch (IOException e) {
      log.error(e);
    } finally {
      if (entity != null) {
        try {
          EntityUtils.consume(entity);
        } catch (IOException e) {
          log.error(e);
        }
      }
      try {
        if (actual != null) {
          actual.close();
        }
      } catch (IOException e) {
        log.error(e);
      }
    }
    return responseBody;
  }

  protected Map<String, String> authorizationHeader() {
    return Map.of(
        PropertiesConfig.AUTHORIZATION_HEADER,
        "Bearer " + scenarioVars.getAsString("access_token"));
  }

  protected Map<String, String> keyloackLoginHeaders() {
    return Map.of("Content-Type", "application/x-www-form-urlencoded");
  }
}
