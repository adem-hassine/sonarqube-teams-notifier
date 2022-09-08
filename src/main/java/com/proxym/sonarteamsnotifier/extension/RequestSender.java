package com.proxym.sonarteamsnotifier.extension;

import com.proxym.sonarteamsnotifier.exceptions.InvalidHttpResponseException;
import com.proxym.sonarteamsnotifier.model.MeasuresContainer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.HttpConnector;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class RequestSender {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private RequestSender(){}
  static {
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private static final Logger LOG = Loggers.get(RequestSender.class);
  public static MeasuresContainer get(String baseUrl, String url, String token) {
    HttpConnector.Builder builder = HttpConnector.newBuilder();
    HttpConnector httpConnector= builder.userAgent("teams-notifier")
      .url(baseUrl)
      .proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress(8080)))
      .credentials(token,null).build();
    try{
      String response = httpConnector.call(new GetRequest(url)).content();
      return objectMapper.readValue(response, MeasuresContainer.class);

    }catch (Exception exception){
      LOG.error("Request failed ! {}",exception.getMessage() );
      throw new InvalidHttpResponseException("Invalid http response from sonar project analysis");
    }

  }
}


