package com.ionos.tests.service;

import io.jtest.utils.clients.http.HttpClient;
import io.jtest.utils.clients.http.Method;
import java.util.Map;

public abstract class HTTPAbstract extends RestService {

  protected HttpClient.Builder sendGet(String address, Map<String, String> headers, String path) {

    return defaultClientBuilder().address(address).path(path).headers(headers).method(Method.GET);
  }

  protected HttpClient.Builder sendGet(String address, Map<String, String> headers) {

    return defaultClientBuilder().address(address).headers(headers).method(Method.GET);
  }

  protected HttpClient.Builder sendGet(
      String address, Map<String, String> headers, String path, String depth) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .queryParam("depth", depth)
        .headers(headers)
        .method(Method.GET);
  }

  protected HttpClient.Builder sendGetWithQueryParams(
      String address, Map<String, String> headers, String path, String queryParam, String value) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .queryParam(queryParam, value)
        .headers(headers)
        .method(Method.GET);
  }

  protected HttpClient.Builder sendGetWithAdditionalHeaders(
      String address,
      Map<String, String> firstHeader,
      Map<String, String> secondHeader,
      String path) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(firstHeader)
        .headers(secondHeader)
        .method(Method.GET);
  }

  protected HttpClient.Builder sendGetWithMultipleQueryParams(
          String address,
          Map<String, String> headers,
          String path,
          String firstQueryParam,
          String firstValue,
          String secondQueryParam,
          String secondValue,
          String thirdQueryParam,
          String thirdValue,
          String fourthQueryParam,
          String fourthValue,
          String fifthQueryParam,
          String fifthValue,
          String sixthQueryParam,
          String sixthValue) {

    return defaultClientBuilder()
            .address(address)
            .path(path)
            .queryParam(firstQueryParam, firstValue)
            .queryParam(secondQueryParam, secondValue)
            .queryParam(thirdQueryParam, thirdValue)
            .queryParam(fourthQueryParam, fourthValue)
            .queryParam(fifthQueryParam, fifthValue)
            .queryParam(sixthQueryParam, sixthValue)
            .headers(headers)
            .method(Method.GET);
  }

  protected HttpClient.Builder sendGetWithTwoQueryParams(
      String address,
      Map<String, String> headers,
      String path,
      String firstQueryParam,
      String firstValue,
      String secondQueryParam,
      String secondValue) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .queryParam(firstQueryParam, firstValue)
        .queryParam(secondQueryParam, secondValue)
        .headers(headers)
        .method(Method.GET);
  }

  protected HttpClient.Builder sendPost(
      String address, Map<String, String> headers, String requestBody, String path) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers, true)
        .method(Method.POST)
        .entity(requestBody);
  }

  protected HttpClient.Builder sendPost(String address, Map<String, String> headers, String path) {

    return defaultClientBuilder().address(address).path(path).headers(headers).method(Method.POST);
  }

  protected HttpClient.Builder sendPost(
      String address, Map<String, String> headers, String requestBody, String path, String depth) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.POST)
        .queryParam("depth", depth)
        .entity(requestBody);
  }

  protected HttpClient.Builder sendPut(
      String address, Map<String, String> headers, String requestBody, String path) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.PUT)
        .entity(requestBody);
  }

  protected HttpClient.Builder sendPatch(
      String address, Map<String, String> headers, String requestBody, String path) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.PATCH)
        .entity(requestBody);
  }

  protected HttpClient.Builder sendPatch(
      String address, Map<String, String> headers, String requestBody, String path, String depth) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.PATCH)
        .queryParam("depth", depth)
        .entity(requestBody);
  }

  protected HttpClient.Builder sendDelete(
      String address, Map<String, String> headers, String path) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.DELETE);
  }

  protected HttpClient.Builder sendDelete(
      String address, Map<String, String> headers, String path, String requestBody) {

    return defaultClientBuilder()
        .address(address)
        .path(path)
        .headers(headers)
        .method(Method.DELETE)
        .entity(requestBody);
  }
}
