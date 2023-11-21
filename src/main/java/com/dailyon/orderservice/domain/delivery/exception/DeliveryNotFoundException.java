package com.dailyon.orderservice.domain.delivery.exception;

import com.dailyon.orderservice.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DeliveryNotFoundException extends CustomException {
  public static final String MESSAGE = "해당 주문번호에 해당하는 배송이 존재하지 않습니다.";

  public DeliveryNotFoundException() {
    super(MESSAGE);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
