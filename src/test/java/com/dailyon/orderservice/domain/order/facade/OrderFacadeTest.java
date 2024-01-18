package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderFacadeTest extends IntegrationTestSupport {

  @Autowired OrderFacade orderFacade;
  @Autowired OrderRepository orderRepository;

  @DisplayName("페이지 정보를 받아 주문 내역 결과를 반환한다.")
  @Test
  void getOrders() {
    // given
    Long memberId = 1L;
    for (int i = 0; i < 10; i++) {
      orderRepository.save(
          createOrder(
              generate(memberId), memberId, "testProducts" + i, (long) (10000 * i), SINGLE));
    }
    // when
    OrderPageResponse orders = orderFacade.getOrders(PageRequest.of(0, 8), SINGLE, memberId);
    // then
    assertThat(orders.getOrders()).isNotEmpty().hasSize(8);
    assertThat(orders.getTotalElements()).isEqualTo(10);
    assertThat(orders.getTotalPages()).isEqualTo(2);
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
}
