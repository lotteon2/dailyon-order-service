package com.dailyon.orderservice.domain.refund.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RefundReader {
  private final RefundRepository refundRepository;

  public List<Refund> readAll(String orderNo) {
    return refundRepository.findByOrderNo(orderNo);
  }

  public int readRefundedPoints(Order order) {
    if (order.getUsedPoints() == 0) return 0;
    return refundRepository.getTotalRefundedPoints(order.getOrderNo());
  }
}
