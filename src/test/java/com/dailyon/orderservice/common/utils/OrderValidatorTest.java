package com.dailyon.orderservice.common.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO;
import com.dailyon.orderservice.domain.torder.exception.InsufficientStockException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.dailyon.orderservice.common.utils.OrderValidator.validateCouponInfo;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateStock;
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
            () ->
                validateCouponInfo(
                    discountPrice, totalPurchaseAmount, Optional.ofNullable(couponDTO)))
        .isInstanceOf(InvalidParamException.class)
        .hasMessage("요청한 값이 올바르지 않습니다.");
  }

  @DisplayName("최대 할인 금액보다 높은 할인금액을 할인 하려고 하는 경우 예외가 발생한다.")
  @Test
  void validateOrderAmountAgainstMaximum() {
    // given
    int discountPrice = 10000;
    int totalPurchaseAmount = 0;
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
            () ->
                validateCouponInfo(
                    discountPrice, totalPurchaseAmount, Optional.ofNullable(couponDTO)))
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
}
