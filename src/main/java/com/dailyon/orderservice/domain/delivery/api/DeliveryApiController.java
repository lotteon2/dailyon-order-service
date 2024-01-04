package com.dailyon.orderservice.domain.delivery.api;

import com.dailyon.orderservice.domain.delivery.api.request.DeliveryCreateRequest;
import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import com.dailyon.orderservice.domain.delivery.service.response.DeliveryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryApiController {
  private final DeliveryService deliveryService;

  @PostMapping("")
  public ResponseEntity<Void> createDelivery(@Valid @RequestBody DeliveryCreateRequest request) {
    deliveryService.createDelivery(request.toServiceRequest());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/orders/{orderId}")
  public ResponseEntity<DeliveryResponse> getDeliveryDetail(
      @PathVariable("orderId") String orderId) {
    return ResponseEntity.ok(deliveryService.getDeliveryDetail(orderId));
  }
}
