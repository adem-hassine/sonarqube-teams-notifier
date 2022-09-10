package com.proxym.sonarteamsnotifier.plugin.extension;

import com.proxym.sonarteamsnotifier.plugin.constants.PayloadUtils;
import com.proxym.sonarteamsnotifier.plugin.exceptions.HttpClientException;
import com.proxym.sonarteamsnotifier.plugin.exceptions.InvalidHttpResponseException;
import com.proxym.sonarteamsnotifier.plugin.webhook.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

class TeamsHttpClient {

  /**
   * Logger.
   */
  private static final Logger LOG = Loggers.get(TeamsHttpClient.class);


  /**
   * The URL for the webhook.
   */
  private URL hook;

  /**
   * The full path of the URL including query string and anchor reference.
   */
  private String path;

  /**
   * The payload to send with the request.
   */
  private Payload payload;

  /**
   * Internal Apache HTTP Client.
   */
  private CloseableHttpClient httpClient;

  /**
   * Whether or not to bypass HTTPS validation.
   */
  private boolean bypassHttpsValidation = false;

  /**
   * The target host of the webhook.
   */
  private HttpHost target;

  /**
   * The HTTP POST request to send.
   */
  private HttpPost httpPost;

  /**
   * Constructor.
   *
   * @param url     The URL of the webhook.
   * @param payload The payload to send to the webhook.
   * @throws MalformedURLException If the URL is malformed.
   */
  private TeamsHttpClient(String url, Payload payload) throws MalformedURLException {
    this.hook = new URL(url);
    this.payload = payload;
  }

  /**
   * Static pattern constructor.
   *
   * @param url     The URL of the webhook.
   * @param payload The payload to send to the webhook.
   * @return The TeamsHttpClient
   * @throws MalformedURLException If the URL is malformed.
   */
  static TeamsHttpClient of(String url, Payload payload) throws MalformedURLException {
    return new TeamsHttpClient(url, payload);
  }

  /**
   * Builds the TeamsHttpClient, preparing it to make the request.
   *
   * @return The TeamsHttpClient
   * @throws UnsupportedEncodingException If the payload is malformed.
   */
  TeamsHttpClient build() throws UnsupportedEncodingException, JsonProcessingException {
    int port = getPort();
    path = getPath();
    httpClient = getHttpClient();
    target = new HttpHost(hook.getHost(), port, hook.getProtocol());
    httpPost = getHttpPost();

    LOG.debug(
      "TeamsHttpClient BUILT"
        + " | Host: " + hook.getHost()
        + " | Port: " + port
        + " | Path: " + path
        + " | Bypass HTTPS Validation: " + bypassHttpsValidation
    );
    return this;
  }

  /**
   * Posts the message to the webhook.
   *
   * @return True on success. False on failure.
   */
  boolean post() {
    boolean success = false;
    try {
      CloseableHttpResponse response = httpClient.execute(target, httpPost);
      int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode < 200 || responseCode > 299) {
        throw new InvalidHttpResponseException("Invalid HTTP Response Code: " + responseCode);
      }

      LOG.info("POST Successful!");
      success = true;
    } catch (Exception e) {
      LOG.error("Failed to send teams message", e);
    } finally {
      try {
        httpClient.close();
      } catch (Exception e) {
        LOG.error("Unable to close HTTP Client", e);
      }
    }

    return success;
  }

  /**
   * Gets the HttpPost request object.
   *
   * @return The HttpPost.
   * @throws UnsupportedEncodingException If the payload is malformed.
   */
  private HttpPost getHttpPost() throws UnsupportedEncodingException, JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    HttpPost tempHttpPost = new HttpPost(path);
    tempHttpPost.setEntity(new StringEntity(PayloadUtils.annotatedAttributesReplace(objectMapper.writeValueAsString(payload))));
    tempHttpPost.setHeader("Accept", "application/json");
    tempHttpPost.setHeader("Content-type", "application/json");
    return tempHttpPost;
  }

  /**
   * Gets the internal HTTP Client to be used for the request.
   *
   * @return The HTTP Client.
   */
  private CloseableHttpClient getHttpClient() {
    try (CloseableHttpClient teamsHttpClient = HttpClients.createDefault()) {
      return teamsHttpClient;
    } catch (IOException e) {
      throw new HttpClientException("Could not instantiate a default http client");
    }
  }


  /**
   * Gets the port of the webhook URL.
   *
   * @return The port of the webhook URL.
   */
  private int getPort() {
    int tempPort = hook.getPort();
    if (tempPort == -1) {
      tempPort = (hook.getProtocol().equals("https") ? 443 : 80);
    }

    return tempPort;
  }

  /**
   * Gets the full path of the webhook URL, including query string and anchor reference.
   *
   * @return The full path of the webhook URL.
   */
  private String getPath() {
    String tempPath = hook.getPath();
    String query = hook.getQuery();
    if (query != null && !query.isEmpty()) {
      tempPath += "?" + query;
    }

    String ref = hook.getRef();
    if (ref != null && !ref.isEmpty()) {
      tempPath += "#" + ref;
    }

    return tempPath;
  }

}
