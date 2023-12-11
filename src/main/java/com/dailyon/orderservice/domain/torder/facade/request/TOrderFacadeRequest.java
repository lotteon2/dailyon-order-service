package com.dailyon.orderservice.domain.torder.facade.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductParam;
import com.dailyon.orderservice.domain.torder.service.request.TOrderServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

public class TOrderFacadeRequest {
  @Getter
  @NoArgsConstructor
  public static class TOrderFacadeCreateRequest {
    private List<CouponInfo> couponInfos;
    private List<OrderProductInfo> orderProductInfos;
    private OrderInfo orderInfo;

    @Builder
    private TOrderFacadeCreateRequest(
        List<CouponInfo> couponInfos,
        List<OrderProductInfo> orderProductInfos,
        OrderInfo orderInfo) {
      this.couponInfos = couponInfos;
      this.orderProductInfos = orderProductInfos;
      this.orderInfo = orderInfo;
    }

    public Map<Long, OrderProductInfo> extractOrderInfoToMap() {
      return orderProductInfos.stream()
          .collect(toMap(OrderProductInfo::getProductId, Function.identity()));
    }

    public List<CouponParam> toCouponParams() {
      return couponInfos.stream().map(CouponInfo::toCouponParam).collect(toUnmodifiableList());
    }

    public List<OrderProductParam> toOrderProductParams() {
      return orderProductInfos.stream()
          .map(OrderProductInfo::toOrderProductParam)
          .collect(toUnmodifiableList());
    }

    public TOrderServiceRequest.OrderInfo toServiceOrderInfo() {
      return TOrderServiceRequest.OrderInfo.builder()
          .deliveryFee(orderInfo.getDeliveryFee())
          .type(orderInfo.getType())
          .totalCouponDiscountPrice(orderInfo.getTotalCouponDiscountPrice())
          .usedPoints(orderInfo.usedPoints)
          .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CouponInfo {
      private Long productId;
      private Long categoryId;
      private Long couponInfoId;

      public CouponParam toCouponParam() {
        return CouponParam.builder()
            .couponInfoId(couponInfoId)
            .productId(productId)
            .categoryId(categoryId)
            .build();
      }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderProductInfo {
      private Long productId;
      private Long sizeId;
      private Integer quantity;
      private String referralCode;

      public OrderProductParam toOrderProductParam() {
        return OrderProductParam.builder().productId(productId).sizeId(sizeId).build();
      }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderInfo {
      private int deliveryFee;
      private int usedPoints;
      private int totalCouponDiscountPrice;
      private OrderType type;
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class TOrderFacadeApproveRequest {
    private String orderId;
    private String pgToken;
  }
}
