package com.dailyon.orderservice.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public abstract class OrderNoGenerator {

  // 오늘 날짜 + memberId + 4자리수 random 수로 주문번호가 결정된다.
  public static String generate(Long memberId) {
    Locale currentLocale = new Locale("KOREAN", "KOREA");
    String pattern = "yyyyMMdd";
    SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
    Integer randomNum = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;
    return formatter.format(new Date()) + memberId.toString() + randomNum;
  }
}
