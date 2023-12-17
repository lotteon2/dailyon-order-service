package com.dailyon.orderservice.domain.torder.api;

import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.facade.TOrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class TOrderApiController {
  private final TOrderFacade tOrderFacade;

  @PostMapping("")
  public ResponseEntity<String> orderReady(
      @RequestHeader(value = "memberId") Long memberId,
      @Valid @RequestBody TOrderCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tOrderFacade.orderReady(request.toOrderFacadeCreateRequest(), memberId));
  }

  @GetMapping("/approve/{orderId}")
  public ResponseEntity<String> approve(
      @PathVariable(name = "orderId") String orderId,
      @Valid TOrderRequest.OrderApproveRequest request) {
    return ResponseEntity.ok(tOrderFacade.orderApprove(request.toFacadeRequest(orderId)));
  }
}
