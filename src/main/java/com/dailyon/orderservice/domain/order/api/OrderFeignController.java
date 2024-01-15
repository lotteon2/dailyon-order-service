package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.domain.order.facade.OrderFacade;
import dailyon.domain.order.clients.ProductRankResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/clients/orders")
public class OrderFeignController {
  private final OrderFacade orderFacade;

  @GetMapping("/ranks")
  public ResponseEntity<List<ProductRankResponse>> getMostSoldProductIds() {
    return ResponseEntity.ok(orderFacade.getMostSoldProducts());
  }
}
