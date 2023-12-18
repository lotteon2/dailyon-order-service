package com.dailyon.orderservice.domain.refund.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AlreadyRefundException extends CustomException {
  private static final String MESSAGE = "이미 환불된 주문상품 입니다.";

  public AlreadyRefundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.BAD_REQUEST;
  }
}
