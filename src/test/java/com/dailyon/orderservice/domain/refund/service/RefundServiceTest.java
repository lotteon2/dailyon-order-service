package com.dailyon.orderservice.domain.refund.service;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class RefundServiceTest extends IntegrationTestSupport {
  @Autowired OrderRepository orderRepository;
  @Autowired OrderDetailRepository orderDetailRepository;
  @Autowired RefundService refundService;

  @DisplayName("주문상세에서 주문아이템 하나를 주문 취소한다.")
  @Test
  void createRefund() {
    // given
    Long memberId = 1L;
    String orderNo = generate(memberId);
    Order order = createOrder(orderNo, memberId, "testProducts", 130000L, SINGLE);
    orderRepository.save(order);
    OrderDetail orderDetail =
        orderDetailRepository.save(
            createOrderDetail(
                order,
                orderNo,
                UUID.randomUUID().toString(),
                1L,
                1L,
                1L,
                "testProducts",
                1,
                "free",
                "MAN",
                "testUrl",
                137000,
                "testCouponName",
                3000));
    orderDetailRepository.save(orderDetail);

    // when
    Refund refund = refundService.createRefund(orderDetail);
    // then

    assertThat(refund).isNotNull();
    assertThat(refund).extracting("price", "points").containsExactlyInAnyOrder(130000, 7000);
  }

  private Order createOrder(
      String orderNo, Long memberId, String productsName, Long totalAmount, OrderType type) {
    return Order.builder()
        .orderNo(orderNo)
        .memberId(memberId)
        .productsName(productsName)
        .totalAmount(totalAmount)
        .usedPoints(7000)
        .type(type)
        .build();
  }

  private OrderDetail createOrderDetail(
      Order order,
      String orderNo,
      String orderDetailNo,
      Long productId,
      Long productSizeId,
      Long couponInfoId,
      String productName,
      Integer productQuantity,
      String productSize,
      String productGender,
      String productImgUrl,
      Integer orderPrice,
      String couponName,
      Integer couponDiscountPrice) {
    return OrderDetail.builder()
        .order(order)
        .orderNo(orderNo)
        .orderDetailNo(orderDetailNo)
        .productId(productId)
        .productSizeId(productSizeId)
        .couponInfoId(couponInfoId)
        .productName(productName)
        .productQuantity(productQuantity)
        .productSize(productSize)
        .productGender(productGender)
        .productImgUrl(productImgUrl)
        .orderPrice(orderPrice)
        .couponName(couponName)
        .couponDiscountPrice(couponDiscountPrice)
        .build();
  }
}
