package com.ionos.tests.service.http;

import io.jtest.utils.clients.http.HttpClient;
import java.util.Map;

public interface IService {

  HttpClient.Builder prepareGet(String address, Map<String, String> headers);

  HttpClient.Builder prepareDeleteById(
      String address, Map<String, String> headers, String datacenterId);
}
