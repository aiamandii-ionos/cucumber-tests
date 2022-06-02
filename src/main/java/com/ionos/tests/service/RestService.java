package com.ionos.tests.service;

import io.jtest.utils.clients.http.HttpClient;
import java.util.Map;

public class RestService {

  protected HttpClient.Builder defaultClientBuilder() {

    return new HttpClient.Builder()
        .headers(
            Map.of(
                "Content-Type", "application/json",
                "Accept", "application/json"));
  }
}
