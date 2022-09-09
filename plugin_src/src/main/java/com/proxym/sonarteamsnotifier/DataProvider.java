package com.proxym.sonarteamsnotifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataProvider {
  protected static final String APPLICATION_PROPERTIES = "application.properties";
  protected static Properties applicationProperties = new Properties();
  protected static final Logger LOGGER = Logger.getLogger(DataProvider.class.getCanonicalName());

  static {
    final ClassLoader classLoader = DataProvider.class.getClassLoader();

    // load metrics properties file as a stream
    try (InputStream input = classLoader.getResourceAsStream(APPLICATION_PROPERTIES)){
      if(input!=null) {
        // load properties from the stream in an adapted structure
        applicationProperties.load(input);
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage(), e);
    }
  }
  public static String getProperty(String name){
    return applicationProperties.getProperty(name);
  }
}
