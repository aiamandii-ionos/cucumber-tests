package com.ionos.tests.service.http;

import com.ionos.tests.service.HTTPAbstract;
import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.common.StringFormat;
import java.util.Map;

public class RequestService extends HTTPAbstract {
  private static final String REQUEST_PATH = "/requests/#[requestId]";

  public HttpClient.Builder prepareGetById(
      String address, Map<String, String> headers, String requestId) {
    String endpoint = getEndpoint(REQUEST_PATH, requestId);
    return sendGet(address, headers, endpoint);
  }

  private String getEndpoint(String template, String requestId) {
    return StringFormat.replaceProps(template, Map.of("requestId", requestId));
  }
}
