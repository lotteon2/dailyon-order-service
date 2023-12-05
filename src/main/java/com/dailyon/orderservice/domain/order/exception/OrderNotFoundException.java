package com.dailyon.orderservice.domain.order.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends CustomException {

  public static final String MESSAGE = "해당 주문번호에 해당하는 주문이 존재하지 않습니다.";

  public OrderNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
