package com.proxym.sonarteamsnotifier.constants;



public class PayloadUtils {

  /**
   * for text value color in teams message.
   */
  public static final String HTML_ELEMENT_WITH_COLOR="<p style='display:inline-block;color:%s;'>%s</p>";
  public static final String HTML_BOLD_DESCRIPTION_TEXT="<p style='display:inline-block;font-weight:bold;'>%s</p>";
  public static final String MESSAGE_CARD="MessageCard";
  public static final String OPEN_URI="OpenUri";
  public static final String SUMMARY="New merge request created to ";
  public static final String BRANCH ="Branch name : %s" ;
  public static final String STATUS=" ,Status : [%s]";
  public static final String GREEN_COLOR="008000";
  public static final String RED_COLOR="9d0208";
  public static final String BLUE_COLOR="4361ee";
  public static final String LAST_COMMIT_DETAILS=String.format(HTML_BOLD_DESCRIPTION_TEXT," , Last commit scan:")+ HTML_ELEMENT_WITH_COLOR;

  private PayloadUtils(){}
}
