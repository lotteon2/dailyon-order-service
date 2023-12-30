package com.dailyon.orderservice.domain.order.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OrderDetailNotFoundException extends CustomException {
  private static final String MESSAGE = "주문상세 정보가 존재하지 않습니다.";

  public OrderDetailNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
