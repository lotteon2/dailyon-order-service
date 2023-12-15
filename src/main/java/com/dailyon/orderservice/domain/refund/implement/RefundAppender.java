package com.dailyon.orderservice.domain.refund.implement;

import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefundAppender {
  private final RefundRepository refundRepository;

  public Refund append(Refund refund) {
    return refundRepository.save(refund);
  }
}
