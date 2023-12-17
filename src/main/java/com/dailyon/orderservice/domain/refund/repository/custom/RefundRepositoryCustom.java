package com.dailyon.orderservice.domain.refund.repository.custom;

import com.dailyon.orderservice.domain.refund.entity.Refund;

import java.util.List;

public interface RefundRepositoryCustom {
  List<Refund> findByOrderNo(String orderNo);

  boolean existByOrderDetailNo(String orderDetailNo);

  int getTotalRefundedPoints(String orderNo);
}
