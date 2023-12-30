package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderDetailRepositoryImplTest extends IntegrationTestSupport {
  @Autowired OrderRepository orderRepository;
  @Autowired OrderDetailRepository orderDetailRepository;

  @DisplayName("주문상세번호로 주문상세 정보를 조회한다.")
  @Test
  void getOrderDetail() {
    // given
    Long memberId = 1L;
    String orderNo = generate(memberId);
    Order order = createOrder(orderNo, memberId, "testProducts", 150000L, SINGLE);
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
                150000,
                "testCouponName",
                0));
    // when
    OrderDetail getOrderDetail =
        orderDetailRepository.findByNoFetch(orderDetail.getOrderDetailNo()).get();
    // then
    assertThat(getOrderDetail).isNotNull();
    assertThat(getOrderDetail.getOrderNo()).isEqualTo(orderNo);
    assertThat(getOrderDetail.getOrderDetailNo()).isEqualTo(orderDetail.getOrderDetailNo());
  }

  private Order createOrder(
      String orderNo, Long memberId, String productsName, Long totalAmount, OrderType type) {
    return Order.builder()
        .orderNo(orderNo)
        .memberId(memberId)
        .productsName(productsName)
        .totalAmount(totalAmount)
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
