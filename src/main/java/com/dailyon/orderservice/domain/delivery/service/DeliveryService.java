package com.dailyon.orderservice.domain.delivery.service;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import com.dailyon.orderservice.domain.delivery.implement.DeliveryAppender;
import com.dailyon.orderservice.domain.delivery.implement.DeliveryReader;
import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
import com.dailyon.orderservice.domain.delivery.service.response.DeliveryResponse;
import com.dailyon.orderservice.domain.delivery.service.response.OrderDeliveryPageResponse;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.implement.OrderReader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DeliveryService {
  private final OrderReader orderReader;
  private final DeliveryAppender deliveryAppender;
  private final DeliveryReader deliveryReader;

  @Transactional
  public void createDelivery(DeliveryServiceRequest request) {
    Order order = orderReader.read(request.getOrderId());
    Delivery delivery = request.toEntity(order);
    deliveryAppender.append(delivery);
  }

  public DeliveryResponse getDeliveryDetail(String orderId) {
    Delivery delivery = deliveryReader.read(orderId);
    return DeliveryResponse.from(delivery);
  }

  public OrderDeliveryPageResponse getOrderDeliveries(Pageable pageable, DeliveryStatus status) {
    Page<Delivery> page = deliveryReader.readWithPaging(pageable, status);

    return OrderDeliveryPageResponse.from(page);
  }
}
