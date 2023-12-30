package com.dailyon.orderservice.domain.order.kafka.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
  private String orderDetailNo;
  private Long productId;
  private Long memberId;
  private int point;
  private double ratingAvg;
}
