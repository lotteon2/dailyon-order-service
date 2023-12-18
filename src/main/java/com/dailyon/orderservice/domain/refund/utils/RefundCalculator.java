package com.dailyon.orderservice.domain.refund.utils;

import com.dailyon.orderservice.common.exception.InvalidParamException;
import com.dailyon.orderservice.domain.order.entity.Order;
import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import lombok.Getter;

@Getter
public enum RefundCalculator {
  EQUAL_REFUND_AND_USED_POINTS {
    @Override
    public int calculate(OrderDetail orderDetail, int totalRefundedAmount) {
      return orderDetail.getOrderPrice();
    }

    @Override
    public int getRefundPoints(int usedPoints, int totalRefundedAmount) {
      return 0;
    }
  },
  NO_REFUND_AND_USED_POINTS {
    @Override
    public int calculate(OrderDetail orderDetail, int totalRefundedAmount) {
      Order order = orderDetail.getOrder();
      int refundablePoints = order.getUsedPoints();
      int refundableOrderPrice = orderDetail.getOrderPrice() - refundablePoints;
      return refundableOrderPrice;
    }

    @Override
    public int getRefundPoints(int usedPoints, int totalRefundedAmount) {
      return usedPoints;
    }
  },
  REFUND_AND_USED_POINTS {
    @Override
    public int calculate(OrderDetail orderDetail, int totalRefundedAmount) {
      int usedPoints = orderDetail.getOrder().getUsedPoints();
      int refundablePoints = usedPoints - totalRefundedAmount;
      int refundableOrderPrice = orderDetail.getOrderPrice() - refundablePoints;
      return refundableOrderPrice;
    }

    @Override
    public int getRefundPoints(int usedPoints, int totalRefundedAmount) {
      return usedPoints - totalRefundedAmount;
    }
  };

  public static RefundCalculator findRefundCalculator(int usedPoints, int totalRefundedPoints) {
    if (totalRefundedPoints == usedPoints) return EQUAL_REFUND_AND_USED_POINTS;

    if (totalRefundedPoints == 0 && usedPoints > 0) return NO_REFUND_AND_USED_POINTS;

    if (totalRefundedPoints > 0 && usedPoints > 0 && totalRefundedPoints < usedPoints)
      return REFUND_AND_USED_POINTS;

    throw new InvalidParamException();
  }

  public abstract int calculate(OrderDetail orderDetail, int totalRefundedAmount);

  public abstract int getRefundPoints(int usedPoints, int totalRefundedAmount);
}
