package com.dailyon.orderservice.domain.order.facade.response;

import com.dailyon.orderservice.domain.order.entity.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class OrderPageResponse {
  private List<OrderResponse> orders;
  private Integer totalPages;
  private Long totalElements;

  private OrderPageResponse(List<OrderResponse> orders, Integer totalPages, Long totalElements) {
    this.orders = orders;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public static OrderPageResponse from(Page<Order> page) {
    List<OrderResponse> orders =
        page.getContent().stream()
            .map(OrderResponse::from)
            .collect(Collectors.toUnmodifiableList());
    return new OrderPageResponse(orders, page.getTotalPages(), page.getTotalElements());
  }
}
