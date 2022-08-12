package com.ionos.tests.service.http;

import com.ionos.tests.service.HTTPAbstract;
import io.jtest.utils.clients.http.*;
import io.jtest.utils.common.StringFormat;
import java.util.Map;

public class RequestService extends HTTPAbstract {
  private static final String REQUEST_PATH = "/requests/#[requestId]";
  private static final String REQUESTS_PATH = "/requests";

  public HttpClient.Builder prepareGetById(
      String address, Map<String, String> headers, String requestId) {
    String endpoint = getEndpoint(REQUEST_PATH, requestId);
    return sendGet(address, headers, endpoint);
  }

  public HttpClient.Builder prepareGet(
          String address, Map<String, String> headers, Integer page, Integer size, String type, String status, String start, String end) {
    return defaultClientBuilder()
            .address(address)
            .path(REQUESTS_PATH)
            .queryParam("page", String.valueOf(page))
            .queryParam("size", String.valueOf(size))
            .queryParam("type", type)
            .queryParam("status", status)
            .queryParam("start", start)
            .queryParam("end", end)
            .headers(headers)
            .method(Method.GET);
  }

  public HttpClient.Builder prepareGetWithoutDate(
          String address, Map<String, String> headers, Integer page, Integer size, String type, String status) {
    return defaultClientBuilder()
            .address(address)
            .path(REQUESTS_PATH)
            .queryParam("page", String.valueOf(page))
            .queryParam("size", String.valueOf(size))
            .queryParam("type", type)
            .queryParam("status", status)
            .headers(headers)
            .method(Method.GET);
  }

  public HttpClient.Builder prepareGetWithoutPageSize(
          String address, Map<String, String> headers, String type, String status, String start, String end) {
    return defaultClientBuilder()
            .address(address)
            .path(REQUESTS_PATH)
            .queryParam("type", type)
            .queryParam("status", status)
            .queryParam("start", start)
            .queryParam("end", end)
            .headers(headers)
            .method(Method.GET);
  }

  private String getEndpoint(String template, String requestId) {
    return StringFormat.replaceProps(template, Map.of("requestId", requestId));
  }
}
