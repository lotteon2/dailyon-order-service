package com.dailyon.orderservice.domain.order.implement;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.common.exception.AuthorizationException;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.exception.OrderDetailNotFoundException;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderReaderTest extends IntegrationTestSupport {
  @Autowired OrderReader orderReader;
  @Autowired OrderRepository orderRepository;
  @Autowired OrderDetailRepository orderDetailRepository;

  @DisplayName("주문상세 번호와 회원 아이디로 회원의 주문 상세 정보를 조회 한다.")
  @Test
  void readDetail() {
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
    OrderDetail getOrderDetail = orderReader.readDetail(orderDetail.getOrderDetailNo(), memberId);
    // then
    assertThat(getOrderDetail).isNotNull();
    assertThat(getOrderDetail)
        .extracting("orderNo", "orderDetailNo", "order.memberId")
        .containsExactlyInAnyOrder(orderNo, orderDetail.getOrderDetailNo(), memberId);
  }

  @DisplayName("다른 유저의 주문상세를 조회할 수 없다.")
  @Test
  void readDetailFailWithUnAuthorization() {
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
    Long anotherMemberId = 2L;
    // when // then
    assertThatThrownBy(
            () -> orderReader.readDetail(orderDetail.getOrderDetailNo(), anotherMemberId))
        .isInstanceOf(AuthorizationException.class)
        .hasMessage("권한이 없습니다.");
  }

  @DisplayName("존재하지 않는 주문 상세 번호로 조회 하려고 할 경우 예외가 발생한다.")
  @Test
  void readDetailFailWithNoExistOrderDetailNo() {
    // given
    Long memberId = 1L;
    String noExistOrderDetailNo = "noExistOrderNo";
    // when // then
    assertThatThrownBy(
            () -> orderReader.readDetail(noExistOrderDetailNo, memberId))
            .isInstanceOf(OrderDetailNotFoundException.class)
            .hasMessage("주문상세 정보가 존재하지 않습니다.");
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
