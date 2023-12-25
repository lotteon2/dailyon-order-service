package com.dailyon.orderservice.domain.torder.api;

import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderRequest.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.facade.TOrderFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class TOrderApiController {
  private final TOrderFacade tOrderFacade;

  @Value("${redirect_url}")
  private String REDIRECT_URL;

  @PostMapping("")
  public ResponseEntity<String> orderReady(
      @RequestHeader(value = "memberId") Long memberId,
      @Valid @RequestBody TOrderCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tOrderFacade.orderReady(request.toOrderFacadeCreateRequest(), memberId));
  }

  @GetMapping("/approve/{orderId}")
  public void approve(
          @PathVariable(name = "orderId") String orderId,
          @Valid TOrderRequest.OrderApproveRequest request, HttpServletResponse response) throws IOException {
    String orderNo = tOrderFacade.orderApprove(request.toFacadeRequest(orderId));
    response.sendRedirect(REDIRECT_URL+"/"+orderNo);
  }
}
