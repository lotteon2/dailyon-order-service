package com.dailyon.orderservice.domain.order.service;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.common.utils.OrderNoGenerator;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends IntegrationTestSupport {

  @Autowired OrderRepository orderRepository;
  @Autowired OrderService orderService;

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
    List<Order> orders = orderService.getOrders(8, memberId);
    // then
    assertThat(orders).isNotEmpty().hasSize(8);
  }

  @DisplayName("사용자의 총 주문 내역 개수를 조회한다.")
  @Test
  void getOrderCount() {
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
    Long orderCount = orderService.getOrderCount(memberId);
    // then
    assertThat(orderCount).isEqualTo(10);
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
