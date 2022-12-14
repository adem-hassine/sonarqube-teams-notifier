package com.proxym.sonarteamsnotifier.extension;

import com.proxym.sonarteamsnotifier.exceptions.InvalidHttpResponseException;
import com.proxym.sonarteamsnotifier.jackson.ObjectMapperConfigurer;
import com.proxym.sonarteamsnotifier.metriccall.MeasuresContainer;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarqube.ws.client.GetRequest;
import org.sonarqube.ws.client.HttpConnector;


public class SonarRequestSender {
  private SonarRequestSender(){}

  private static final Logger LOG = Loggers.get(SonarRequestSender.class);

  /**
   * To get project scan history with sonarqube http client.
   */
  public static MeasuresContainer get(String baseUrl, String url, String token) {
    HttpConnector.Builder builder = HttpConnector.newBuilder();
    HttpConnector httpConnector= builder.userAgent("teams-notifier")
      .url(baseUrl)
      .credentials(token,null).build();
    try{
      String response = httpConnector.call(new GetRequest(url)).content();
      return ObjectMapperConfigurer.objectMapper.readValue(response, MeasuresContainer.class);

    }catch (Exception exception){
      LOG.error("Request failed ! {}",exception.getMessage() );
      throw new InvalidHttpResponseException("Invalid http response from sonar project analysis");
    }

  }
}


