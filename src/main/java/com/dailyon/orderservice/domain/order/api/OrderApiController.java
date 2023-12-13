package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import com.dailyon.orderservice.domain.order.facade.response.OrderDetailResponse;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderApiController {
  private final OrderFacade orderFacade;

  @GetMapping("")
  public ResponseEntity<OrderPageResponse> getOrders(
      @RequestHeader(value = "memberId") Long memberId,
      @PageableDefault(size = 8) Pageable pageable) {
    return ResponseEntity.ok(orderFacade.getOrders(pageable, memberId));
  }

  @GetMapping("/{orderNo}")
  public ResponseEntity<List<OrderDetailResponse>> getOrderDetails(
      @RequestHeader(value = "memberId") Long memberId,
      @PathVariable(name = "orderNo") String orderNo) {
    return ResponseEntity.ok(orderFacade.getOrderDetails(orderNo, memberId));
  }
}
