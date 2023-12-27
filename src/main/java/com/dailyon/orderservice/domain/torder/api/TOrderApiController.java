package com.dailyon.orderservice.domain.torder.api;

import com.dailyon.orderservice.domain.torder.api.request.TOrderDto;
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
      @Valid @RequestBody TOrderDto.TOrderCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(tOrderFacade.orderReady(request, memberId));
  }

  @GetMapping("/approve/{orderNo}")
  public void approve(
      @PathVariable(name = "orderNo") String orderNo,
      @Valid TOrderDto.OrderApproveRequest request,
      HttpServletResponse response)
      throws IOException {
    String orderToken = tOrderFacade.orderApprove(request, orderNo);
    response.setStatus(HttpStatus.CREATED.value());
    response.sendRedirect(REDIRECT_URL + "/" + orderToken);
  }
}
