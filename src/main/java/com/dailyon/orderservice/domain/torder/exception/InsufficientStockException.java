package com.dailyon.orderservice.domain.torder.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends CustomException {
  public static final String MESSAGE = "재고가 부족합니다.";

  public InsufficientStockException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
