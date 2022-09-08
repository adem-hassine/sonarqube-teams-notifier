package com.proxym.sonarteamsnotifier.utils;

import lombok.Getter;
//TODO add .properties file
@Getter
public class EndpointProvider {
  private static final String MEASURES_SEARCH_ENDPOINT= "/api/measures/search_history";
  private static final String FROM="?from=";
  private static final String COMPONENT= "&component=";
  private static final String FIXED_PARAMS="&metrics=bugs%2Cvulnerabilities%2Csqale_index%2Cduplicated_lines_density%2Cncloc%2Ccoverage%2Ccode_smells%2Creliability_rating%2Csecurity_rating%2Csqale_rating&ps=1000";
  private static final String SEMICOLON_DATE_REPLACER ="%3A";
  public static String measuresDetails(String projectKey){
    String date = DateGenerator.parseAfterGeneration();
    String endpointDate = date.replace(":",SEMICOLON_DATE_REPLACER);
    return MEASURES_SEARCH_ENDPOINT + FROM + endpointDate + "%2B0000" + COMPONENT + projectKey + FIXED_PARAMS;

  }
  private EndpointProvider(){}

}
