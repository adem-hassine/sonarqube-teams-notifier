package com.proxym.sonarteamsnotifier.constants;



public class PayloadUtils {

  /**
   * for text value color in teams message.
   */
  public static final String HTML_ELEMENT_WITH_COLOR="<p style='display:inline-block;color:%s;'>%s</p>";
  /**
   * Inorder to format previous value indicator.
   */
  public static final String HTML_BOLD_DESCRIPTION_TEXT="<p style='display:inline-block;font-weight:bold;'>%s</p>";
  /**
   * Webhook utility for providing message card templpate.
   */
  public static final String MESSAGE_CARD="MessageCard";
  /**
   * Webhook utility for the action type.
   */
  public static final String OPEN_URI="OpenUri";
  /**
   * Latest version message.
   */
  public static final String SUMMARY="Latest version scan for project : ";
  public static final String BRANCH ="Branch name : %s" ;
  public static final String STATUS=" ,Status : [%s]";
  public static final String GREEN_COLOR="008000";
  public static final String RED_COLOR="9d0208";
  public static final String BLUE_COLOR="4361ee";
  /**
   * Pagination request params format for sonarqube.
   */
  public static final String PAGINATION_PARAMS="&ps=%d&p=%d";
  public static final String LAST_COMMIT_DETAILS=String.format(HTML_BOLD_DESCRIPTION_TEXT," , Difference with previous version :")+ HTML_ELEMENT_WITH_COLOR;
  public static final String ALERT_STATUS_METRIC="alert_status";
  private PayloadUtils(){}
}
