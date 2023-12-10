package com.dailyon.orderservice.domain.torder.exception;

import com.dailyon.orderservice.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InsufficientPointException extends CustomException {
  public static final String MESSAGE = "포인트가 부족합니다.";

  public InsufficientPointException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
