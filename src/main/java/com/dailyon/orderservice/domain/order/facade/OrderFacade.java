package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.request.DeliveryServiceRequest;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.order.facade.response.OrderDetailResponse;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import com.dailyon.orderservice.domain.order.service.OrderService;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.service.TOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderFacade {
  private final OrderService orderService;
  private final TOrderService tOrderService;
  private final DeliveryService deliveryService;

  public String orderCreate(TOrder tOrder, OrderEvent event) {
    orderService.createOrder(tOrder);
    deliveryService.createDelivery(DeliveryServiceRequest.from(tOrder.getDelivery()));
    tOrderService.deleteTOrder(tOrder.getId());
    return tOrder.getId();
  }

  public OrderPageResponse getOrders(Pageable pageable, Long memberId) {
    Page<Order> page = orderService.getOrders(pageable, memberId);
    return OrderPageResponse.from(page);
  }

  public List<OrderDetailResponse> getOrderDetails(String orderNo, Long memberId) {
    List<OrderDetail> orderDetails = orderService.getOrderDetails(orderNo, memberId);
    return extractOrderDetailResponses(orderDetails);
  }

  public void cancelOrderDetail(String OrderDetailNo, Long memberId) {
    OrderDetail orderDetail = orderService.cancelOrderDetail(OrderDetailNo, memberId);
  }

  private List<OrderDetailResponse> extractOrderDetailResponses(List<OrderDetail> orderDetails) {
    return orderDetails.stream()
        .map(OrderDetailResponse::from)
        .collect(Collectors.toUnmodifiableList());
  }
}
