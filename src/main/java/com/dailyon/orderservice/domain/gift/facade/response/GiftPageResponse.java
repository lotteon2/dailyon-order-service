package com.dailyon.orderservice.domain.gift.facade.response;

import com.dailyon.orderservice.domain.gift.entity.Gift;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
@ToString
public class GiftPageResponse {
  private List<GiftResponse> gifts;
  private Integer totalPages;
  private Long totalElements;

  private GiftPageResponse(List<GiftResponse> gifts, Integer totalPages, Long totalElements) {
    this.gifts = gifts;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public static GiftPageResponse from(Page<Gift> page) {
    List<GiftResponse> gifts = page.map(GiftResponse::from).toList();
    return new GiftPageResponse(gifts, page.getTotalPages(), page.getTotalElements());
  }
}
