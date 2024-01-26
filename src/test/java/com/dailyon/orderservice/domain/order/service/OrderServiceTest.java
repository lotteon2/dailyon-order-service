package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.common.exception.AuthorizationException;
import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.entity.enums.OrderDetailStatus;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.exception.CancellationNotAllowedException;
import com.dailyon.orderservice.domain.order.repository.OrderDetailRepository;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrder;
import com.dailyon.orderservice.domain.order.dynamo.document.TOrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.UUID;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends IntegrationTestSupport {

  @Autowired OrderRepository orderRepository;
  @Autowired OrderDetailRepository orderDetailRepository;
  @Autowired OrderService orderService;
  @Autowired EntityManager em;

  @DisplayName("임시 주문 정보를 통해 주문을 생성한다.")
  @Test
  void createOrder() {
    // given
    Long memberId = 1L;
    String orderId = OrderNoGenerator.generate(1L);
    TOrder tOrder = createOrder(orderId, memberId, 84000L, SINGLE);

    TOrderDetail tOrderDetail1 =
        createTOrderDetail(
            orderId, 1L, 2L, 3L, "나이키신발", 1, "260", "MAN", "testUrl", 54000, "10%할인쿠폰", 6000);

    TOrderDetail tOrderDetail2 =
        createTOrderDetail(
            orderId, 2L, 3L, null, "나이키 양말", 1, "260", "MAN", "testUrl", 30000, null, 0);

    tOrder.setOrderDetails(List.of(tOrderDetail1, tOrderDetail2));
    // when
    Order order = orderService.createOrder(tOrder);
    // then
    assertThat(order.getOrderNo()).isNotNull().isEqualTo(tOrder.getId());
    assertThat(order.getTotalAmount()).isEqualTo(tOrder.getTotalAmount());
  }

  @DisplayName("pageSize 만큼 주문 내역을 조회 한다.")
  @Test
  void getOrdersWithPaging() {
    // given
    Long memberId = 1L;
    for (int i = 0; i < 10; i++) {
      orderRepository.save(
          createOrder(
              generate(memberId),
              memberId,
              "testProducts" + i,
              (long) (10000 * i),
              OrderType.SINGLE));
    }
    // when
    PageRequest page = PageRequest.of(0, 8);
    Page<Order> orders = orderService.getOrders(page, SINGLE, memberId);
    // then
    assertThat(orders.getContent()).isNotEmpty().hasSize(8);
  }

  @DisplayName("주문 상세 내역을 조회 한다.")
  @Test
  void getOrderDetails() {
    // given
    Long memberId = 1L;
    String orderNo = generate(memberId);
    Order order = createOrder(orderNo, memberId, "testProducts", 150000L, SINGLE);
    orderRepository.save(order);
    for (int i = 0; i < 5; i++) {
      orderDetailRepository.save(
          createOrderDetail(
              order,
              orderNo,
              UUID.randomUUID().toString(),
              1L + i,
              1L,
              1L,
              "testProducts",
              1,
              "free",
              "MAN",
              "testUrl",
              2000 * i,
              "testCouponName",
              0));
    }

    // when
    em.clear();
    List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo, memberId);
    // then
    assertThat(orderDetails).isNotEmpty().hasSize(5);
  }

  @DisplayName("다른 사람의 주문 내역을 조회할 수 없다.")
  @Test
  void getOrderDetailsWithNoAuthorization() {
    // given
    Long memberId = 1L;
    String orderNo = generate(memberId);
    Order order = createOrder(orderNo, memberId, "testProducts", 150000L, SINGLE);
    orderRepository.save(order);

    assertThatThrownBy(() -> orderService.getOrderDetails(orderNo, 2L))
        .isInstanceOf(AuthorizationException.class)
        .hasMessage("권한이 없습니다.");
  }

  @DisplayName("배송 전인 주문 상품을 취소한다.")
  @Test
  void orderDetailCancel() {
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
                20000,
                "testCouponName",
                0));
    // when
    OrderDetail changedOrderDetail =
        orderService.cancelOrderDetail(orderDetail.getOrderDetailNo(), memberId);
    // then
    assertThat(changedOrderDetail.getStatus()).isEqualTo(OrderDetailStatus.CANCEL);
  }

  @DisplayName("다른 사람의 주문 상세를 취소할 수 없다.")
  @Test
  void orderDetailCancelWithNoAuthorization() {
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
                20000,
                "testCouponName",
                0));

    Long otherMemberId = 2L;
    // when // then
    assertThatThrownBy(
            () -> orderService.cancelOrderDetail(orderDetail.getOrderDetailNo(), otherMemberId))
        .isInstanceOf(AuthorizationException.class)
        .hasMessage("권한이 없습니다.");
  }

  @DisplayName("배송 전 상태가 아닌 주문 상품은 취소할 수 없다.")
  @Test
  void orderDetailCancelIfNotBeforeDeliveryStatus() {
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
                20000,
                "testCouponName",
                0));

    orderDetail.completeDelivery();
    // when // then
    assertThatThrownBy(
            () -> orderService.cancelOrderDetail(orderDetail.getOrderDetailNo(), memberId))
        .isInstanceOf(CancellationNotAllowedException.class)
        .hasMessage("배송전 상태의 주문상품만 취소가 가능합니다.");
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

  private TOrder createOrder(String orderId, Long memberId, Long totalAmount, OrderType type) {
    return TOrder.builder()
        .id(orderId)
        .memberId(memberId)
        .productsName("testProducts")
        .totalAmount(totalAmount)
        .type(type.name())
        .build();
  }

  private TOrderDetail createTOrderDetail(
      String orderNo,
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
    return TOrderDetail.builder()
        .orderNo(orderNo)
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
