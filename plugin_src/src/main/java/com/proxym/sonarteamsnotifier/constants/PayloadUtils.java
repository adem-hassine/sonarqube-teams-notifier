package com.proxym.sonarteamsnotifier.constants;


import java.util.ArrayList;
import java.util.List;

public class PayloadUtils {
  public static final String LOGO_URL="https://avatars.githubusercontent.com/u/1303819?s=200&v=4";
  protected static final List<String> ANNOTATED_ATTRIBUTES = new ArrayList<>();
  protected static final List<String> INNER_JSON_ANNOTATED_ATTRIBUTES = new ArrayList<>();
  public static final String GREEN_COLOR ="008000";
  public static final String RED_COLOR= "bc4749";
  public static final String MESSAGE_CARD="MessageCard";
  public static final String OPEN_URI="OpenUri";
  public static final String SUMMARY="New merge request created to ";
  static {
    ANNOTATED_ATTRIBUTES.add("type");
    for (String attribute:ANNOTATED_ATTRIBUTES){
      INNER_JSON_ANNOTATED_ATTRIBUTES.add("\"".concat(attribute).concat("\":"));
    }

  }
  public static String annotatedAttributesReplace(String json){
   for (String annotatedAttribute:INNER_JSON_ANNOTATED_ATTRIBUTES){
     json = json.replace(annotatedAttribute,"\"@".concat(annotatedAttribute).concat("\":"));
   }
   return json;
  }
  private PayloadUtils(){}
}
