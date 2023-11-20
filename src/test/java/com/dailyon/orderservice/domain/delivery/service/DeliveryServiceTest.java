package com.dailyon.orderservice.domain.delivery.service;

import com.dailyon.orderservice.domain.delivery.api.request.DeliveryCreateRequest;
import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.repository.DeliveryRepository;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DeliveryServiceTest {
  @Autowired DeliveryService deliveryService;
  @Autowired EntityManager entityManager;
  @Autowired DeliveryRepository deliveryRepository;
  @Autowired OrderRepository orderRepository;

  @AfterEach
  void tearDown() {
    deliveryRepository.deleteAllInBatch();
  }

  @DisplayName("배송 정보를 입력 받아 배송을 등록한다.")
  @Test
  void createDelivery() {
    // given
    Order order = createOrder(1L, 15000, "나이키슬리퍼", OrderType.SINGLE);
    DeliveryCreateRequest request =
        new DeliveryCreateRequest(
            order.getId(), "홍길동", "201-201", "서울특별시 강서구5길", "강서아파트111호", null);
    // when
    deliveryService.createDelivery(request);
    // then
    List<Delivery> deliveries = deliveryRepository.findAll();
    assertThat(deliveries).isNotEmpty().hasSize(1);
  }

  private Order createOrder(
      Long memberId, Integer orderPrice, String productsName, OrderType type) {
    return orderRepository.save(
        Order.builder()
            .memberId(memberId)
            .orderPrice(orderPrice)
            .productsName(productsName)
            .type(type)
            .build());
  }
}
