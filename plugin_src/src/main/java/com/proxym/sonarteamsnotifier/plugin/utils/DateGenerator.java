package com.proxym.sonarteamsnotifier.plugin.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateGenerator {
  private static final String DATE_FORMAT="yyyy-MM-dd'T'HH:mm:ss";
  private DateGenerator(){}
  public static String parseAfterGeneration()  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.HOUR, -1);
    Date oneHourBack = cal.getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
    return formatter.format(oneHourBack);
  }
}
