package com.example.jooq.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateParser {

  public static Date parse(String str) {
    LocalDate ld = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    Instant i = ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
    return Date.from(i);
  }

  public static java.sql.Date parseSQL(String str) {
    LocalDate ld = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return java.sql.Date.valueOf(ld);
  }

  public static java.sql.Date parseSQL(LocalDateTime dt) {
    return java.sql.Date.valueOf(dt.toLocalDate());
  }

  public static String format(Date date) {
    if (date == null) {
      return "---";
    }
    Instant i = date.toInstant();
    LocalDate ld = LocalDateTime.ofInstant(i, ZoneId.systemDefault()).toLocalDate();
    return ld.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

}
