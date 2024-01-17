package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import com.dailyon.orderservice.domain.order.facade.response.OrderPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders")
public class OrderAdminApiController {
  private final OrderFacade orderFacade;

  @GetMapping("")
  public ResponseEntity<OrderPageResponse> getOrders(
      @RequestHeader(value = "memberId") Long memberId,
      @RequestHeader(value = "role") String role,
      @PageableDefault(size = 8) Pageable pageable) {
    return ResponseEntity.ok(orderFacade.getOrders(pageable, role, memberId));
  }
}
