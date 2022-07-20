package com.ionos.tests.service.http;

import com.ionos.tests.service.HTTPAbstract;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.common.StringFormat;
import java.util.Map;

public class ServerService extends HTTPAbstract implements IService {

  private static final String SERVERS_PATH = "/servers";
  private static final String SERVER_PATH = "/servers/#[serverId]";

  public HttpClient.Builder retrieveAllServers(String address, Map<String, String> headers) {
    return sendGet(address, headers, SERVERS_PATH);
  }

  public HttpClient.Builder prepareCreate(
      String address, Map<String, String> headers, String requestBody) {
    return sendPost(address, headers, requestBody, SERVERS_PATH);
  }

  public HttpClient.Builder prepareGet(String address, Map<String, String> headers) {
    return sendGet(address, headers, SERVERS_PATH);
  }

  public HttpClient.Builder prepareDeleteById(
      String address, Map<String, String> headers, String serverId) {
    String endpoint = getEndpoint(SERVER_PATH, serverId);
    return sendDelete(address, headers, endpoint);
  }

  public HttpClient.Builder prepareGetById(
      String address, Map<String, String> headers, String serverId) {
    String endpoint = getEndpoint(SERVER_PATH, serverId);
    return sendGet(address, headers, endpoint);
  }

  public HttpClient.Builder updateServerById(
      String address, Map<String, String> headers, String requestBody, String serverId) {
    String endpoint = getEndpoint(SERVER_PATH, serverId);
    return sendPut(address, headers, requestBody, endpoint);
  }

  public HttpClient.Builder login(String address, Map<String, String> headers, String requestBody) {
    return sendPost(address, headers, requestBody, "");
  }

  private String getEndpoint(String template, String serverId) {
    return StringFormat.replaceProps(template, Map.of("serverId", serverId));
  }
}
