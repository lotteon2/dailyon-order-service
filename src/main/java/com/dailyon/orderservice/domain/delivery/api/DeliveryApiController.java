package com.dailyon.orderservice.domain.delivery.api;

import com.dailyon.orderservice.domain.delivery.api.request.DeliveryCreateRequest;
import com.dailyon.orderservice.domain.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryApiController {
  private final DeliveryService deliveryService;

  @PostMapping("")
  public ResponseEntity<Void> createDelivery(@Valid @RequestBody DeliveryCreateRequest request) {
    deliveryService.createDelivery(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}