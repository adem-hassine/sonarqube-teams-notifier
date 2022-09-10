package com.proxym.sonarteamsnotifier.utils;

import com.proxym.sonarteamsnotifier.DataProvider;
import com.proxym.sonarteamsnotifier.constants.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Provides sonar endpoint to retrieve required mesures.
 */
public class EndpointProvider {
  private static final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";


  public static String measuresDetails(String projectKey,String metrics){
    String date =parseAfterGeneration();
    return String.format(DataProvider.getProperty(Constants.MEASURES_ENDPOINT),date,projectKey,metrics);
  }
  public static String parseAfterGeneration()  {
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.format(new Date()) + "%2B0000";
  }
  private EndpointProvider(){}
}
