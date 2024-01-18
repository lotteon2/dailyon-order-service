package com.dailyon.orderservice.domain.order.repository.custom;

import com.dailyon.orderservice.IntegrationTestSupport;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.dailyon.orderservice.common.utils.OrderNoGenerator.generate;
import static com.dailyon.orderservice.domain.order.entity.enums.OrderType.SINGLE;
import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryImplTest extends IntegrationTestSupport {

  @Autowired OrderRepository orderRepository;

  @DisplayName("pageSize 만큼 주문 내역을 조회 한다.")
  @Test
  void getOrdersWithPaging() {
    // given
    Long memberId = 1L;
    for (int i = 0; i < 10; i++) {
      orderRepository.save(
          createOrder(
              generate(memberId), memberId, "testProducts" + i, (long) (10000 * i), SINGLE));
    }
    // when
    Page<Order> orders = orderRepository.findAllWithPaging(PageRequest.of(0, 8), SINGLE, memberId);
    // then
    assertThat(orders.getContent()).isNotEmpty().hasSize(8);
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
