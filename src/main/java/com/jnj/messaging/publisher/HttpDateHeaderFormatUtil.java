package com.jnj.messaging.publisher;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class HttpDateHeaderFormatUtil {
  public static String nowAsHttpDateString() {
    return timeAsHttpDateString(ZonedDateTime.now(ZoneId.of("UTC")));
  }

  public static String timeAsHttpDateString(ZonedDateTime gmtTime) {
    return gmtTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.n z"));
  }  
}
