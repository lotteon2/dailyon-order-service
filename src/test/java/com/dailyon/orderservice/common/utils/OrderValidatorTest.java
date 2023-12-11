package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO;
import com.dailyon.orderservice.domain.torder.exception.InsufficientPointException;
import com.dailyon.orderservice.domain.torder.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.dailyon.orderservice.common.utils.OrderValidator.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderValidatorTest {

  @DisplayName("최소 주문 금액 보다 적은 금액을 주문한 경우 예외가 발생한다.")
  @Test
  void validateOrderAmountAgainstMinimum() {
    // given
    int discountPrice = 0;
    int totalPurchaseAmount = 5000;
    CouponDTO.ProductCouponDTO couponDTO =
        CouponDTO.ProductCouponDTO.builder()
            .productId(1L)
            .couponInfoId(1L)
            .minPurchaseAmount(6000L)
            .discountType("FIXED_AMOUNT")
            .couponName("3000원 할인 쿠폰")
            .maxDiscountAmount(6000L)
            .discountValue(3000L)
            .build();
    //        int total

    // when // then
    assertThatThrownBy(
            () -> validateCouponInfo(totalPurchaseAmount, Optional.ofNullable(couponDTO)))
        .isInstanceOf(InvalidParamException.class)
        .hasMessage("요청한 값이 올바르지 않습니다.");
  }

  @DisplayName("주문 수량 보다 재고가 부족한 경우 예외가 발생한다.")
  @Test
  void InsufficientStock() {
    // given
    int quantity = 21;
    int stock = 20;
    // when // then
    assertThatThrownBy(() -> validateStock(quantity, stock))
        .isInstanceOf(InsufficientStockException.class)
        .hasMessage("재고가 부족합니다.");
  }

  @DisplayName("회원이 가지고 있는 포인트 보다 더 높은 포인트 할인금액을 적용 하려고 할 경우 예외가 발생한다.")
  @Test
  void InsufficientPoints() {
    // given
    int usedPoints = 10000;
    int memberPoints = 5000;
    // when // then
    assertThatThrownBy(() -> validateMemberPoint(usedPoints, memberPoints))
        .isInstanceOf(InsufficientPointException.class)
        .hasMessage("포인트가 부족합니다.");
  }
}
