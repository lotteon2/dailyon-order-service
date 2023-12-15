package com.dailyon.orderservice.domain.order.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class CancellationNotAllowedException extends CustomException {
  public static final String MESSAGE = "배송전 상태의 주문상품만 취소가 가능합니다.";

  public CancellationNotAllowedException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.BAD_REQUEST;
  }
}
