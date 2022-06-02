package com.ionos.tests.config;

import io.jtest.utils.common.ResourceUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.*;

public class PropertiesConfig {

  private static final Logger LOG = LogManager.getLogger();

  private static Properties loadConfig() {
    LOG.info("Get cucumber config");
    try {
      return ResourceUtils.readProps("app.properties");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static final Properties properties = loadConfig();
  public static final String AUTHORIZATION_HEADER = properties.getProperty("authorization");

  public static final String TOKEN =
      createToken(properties.getProperty("client_id"), properties.getProperty("client_secret"));

  public static final String GRANT_TYPE = properties.getProperty("grant_type");

  public static final String CLIENT_ID = properties.getProperty("client_id");
  public static final String CLIENT_SECRET = properties.getProperty("client_secret");

  public static final String SERVER_AUTH_URL = properties.getProperty("auth_server_url");

  public static String API_URL = properties.getProperty("api_url");

  public static String createToken(String username, String password) {
    return "Basic "
        + new String(
            Base64.encodeBase64((username + ":" + password).getBytes(StandardCharsets.ISO_8859_1)));
  }
}
