package com.dailyon.orderservice.domain.refund.service;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.implement.RefundAppender;
import com.dailyon.orderservice.domain.refund.implement.RefundReader;
import com.dailyon.orderservice.domain.refund.utils.RefundCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dailyon.orderservice.domain.refund.utils.RefundCalculator.findRefundCalculator;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefundService {
  private final RefundReader refundReader;
  private final RefundAppender refundAppender;

  @Transactional
  public Refund createRefund(OrderDetail orderDetail) {
    Order order = orderDetail.getOrder();
    int totalRefundedAmount = refundReader.readRefundedPoints(order);
    RefundCalculator calculator = findRefundCalculator(order.getUsedPoints(), totalRefundedAmount);
    int refundableAmount = calculator.calculate(orderDetail, totalRefundedAmount);
    int refundPoints = calculator.getRefundPoints(order.getUsedPoints(), totalRefundedAmount);
    Refund refund = refundAppender.append(orderDetail, refundPoints, refundableAmount);
    return refund;
  }
}
