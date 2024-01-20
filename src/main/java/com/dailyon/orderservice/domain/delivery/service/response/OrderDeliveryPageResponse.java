package com.dailyon.orderservice.domain.delivery.service.response;

import com.dailyon.orderservice.domain.delivery.entity.Delivery;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDeliveryPageResponse {
  private List<OrderDeliveryResponse> deliveries;
  private Integer totalPages;
  private Long totalElements;

  private OrderDeliveryPageResponse(
      List<OrderDeliveryResponse> deliveries, Integer totalPages, Long totalElements) {
    this.deliveries = deliveries;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public static OrderDeliveryPageResponse from(Page<Delivery> page) {
    List<OrderDeliveryResponse> deliveries =
        page.getContent().stream()
            .map(OrderDeliveryResponse::from)
            .collect(Collectors.toUnmodifiableList());
    return new OrderDeliveryPageResponse(deliveries, page.getTotalPages(), page.getTotalElements());
  }
}
