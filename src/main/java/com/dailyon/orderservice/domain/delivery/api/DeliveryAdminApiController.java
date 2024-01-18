package com.dailyon.orderservice.domain.delivery.api;

import com.dailyon.orderservice.domain.delivery.entity.enums.DeliveryStatus;
import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.response.OrderDeliveryPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deliveries")
public class DeliveryAdminApiController {
  private final DeliveryService deliveryService;

  @GetMapping("")
  public ResponseEntity<OrderDeliveryPageResponse> getDeliveries(
      @RequestParam(name = "status", required = false) DeliveryStatus status,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(deliveryService.getOrderDeliveries(pageable, status));
  }
}
