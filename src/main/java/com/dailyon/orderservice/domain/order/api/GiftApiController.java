package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.domain.order.facade.GiftFacade;
import com.dailyon.orderservice.domain.order.facade.response.GiftPageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gifts")
public class GiftApiController {
  private final GiftFacade giftFacade;

  @GetMapping("/me")
  public ResponseEntity<GiftPageResponse> getReceiverGifts(
      @RequestHeader(value = "memberId") Long memberId,
      @PageableDefault(size = 8) Pageable pageable) {
    return ResponseEntity.ok(giftFacade.getReceiverGifts(memberId, pageable));
  }
}
