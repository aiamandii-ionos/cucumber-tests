package com.ionos.tests.context.hooks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ionos.tests.config.PropertiesConfig;
import com.ionos.tests.service.http.*;
import io.jtest.utils.clients.http.HttpClient;
import java.io.IOException;
import java.util.*;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResourceCleanup {
  private static final Logger LOG = LogManager.getLogger();

  public static final ServerService SERVER_SERVICE = new ServerService();
  public static final Map<String, String> HEADER =
      Map.of(PropertiesConfig.AUTHORIZATION_HEADER, "Bearer " + PropertiesConfig.TOKEN);

  public void clean() throws IOException {
    List<String> serverList = parseToList(getBodyResponse(SERVER_SERVICE));

    LOG.info("-------- DELETE SERVERS------------");
    deleteResources(SERVER_SERVICE, serverList);
  }

  public String getBodyResponse(IService service, String url) throws IOException {
    HttpClient client = service.prepareGet(url, HEADER).build();
    HttpResponse response = client.execute();

    return new BasicResponseHandler().handleResponse(response);
  }

  public String getBodyResponse(IService service) throws IOException {

    return getBodyResponse(service, PropertiesConfig.API_URL);
  }

  public List<String> parseToList(String stringToParse) throws JsonProcessingException {

    List<String> list = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    JsonNode jsonNode = mapper.readTree(stringToParse);
    Iterator<JsonNode> iterator = jsonNode.withArray("items").elements();
    while (iterator.hasNext()) {
      JsonNode ele = iterator.next();
      list.add(ele.get("id").textValue());
    }

    return list;
  }

  public void deleteResources(IService service, List<String> listOfIdResources) throws IOException {
    deleteResources(service, PropertiesConfig.API_URL, listOfIdResources);
  }

  public void deleteResources(IService service, String url, List<String> listOfIdResources)
      throws IOException {
    if (!listOfIdResources.isEmpty()) {
      Iterator<String> iterator = listOfIdResources.iterator();
      while (iterator.hasNext()) {
        String next = iterator.next();
        HttpClient client = service.prepareDeleteById(url, HEADER, next).build();
        HttpResponse response = client.execute();
        var value = new BasicResponseHandler().handleResponse(response);
        LOG.info(value);
      }
    }
  }
}
