package com.dailyon.orderservice.domain.refund.implement;

import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import com.dailyon.orderservice.domain.refund.repository.RefundRepository;
import com.dailyon.orderservice.domain.refund.utils.RefundCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dailyon.orderservice.domain.refund.utils.RefundCalculator.findRefundCalculator;

@RequiredArgsConstructor
@Component
public class RefundAppender {
  private final RefundRepository refundRepository;

  public Refund append(OrderDetail orderDetail, int refundPoints, int refundableAmount) {

    Refund refund =
        Refund.builder()
            .order(orderDetail.getOrder())
            .orderDetail(orderDetail)
            .price(refundableAmount)
            .points(refundPoints)
            .build();
    return refundRepository.save(refund);
  }
}
