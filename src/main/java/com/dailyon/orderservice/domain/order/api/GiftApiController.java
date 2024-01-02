package com.dailyon.orderservice.domain.order.api;

import com.dailyon.orderservice.domain.order.api.request.GiftDto;
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

  @GetMapping("/receiver")
  public ResponseEntity<GiftPageResponse> getReceiverGifts(
      @RequestHeader(value = "memberId") Long memberId,
      @PageableDefault(size = 4) Pageable pageable) {
    return ResponseEntity.ok(giftFacade.getReceiverGifts(memberId, pageable));
  }

  @GetMapping("/sender")
  public ResponseEntity<GiftPageResponse> getSenderGifts(
      @RequestHeader(value = "memberId") Long memberId,
      @PageableDefault(size = 4) Pageable pageable) {
    return ResponseEntity.ok(giftFacade.getSenderGifts(memberId, pageable));
  }

  @PostMapping("/accept")
  public ResponseEntity<Void> acceptGift(
      @RequestHeader(value = "memberId") Long memberId,
      @RequestBody GiftDto.createDelivery request) {
    giftFacade.accept(request, memberId);
    return ResponseEntity.ok().build();
  }
}
