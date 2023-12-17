package com.dailyon.orderservice.domain.order.kafka.event.dto;

import com.dailyon.orderservice.domain.order.entity.OrderDetail;
import com.dailyon.orderservice.domain.refund.entity.Refund;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefundDTO {

  private ProductInfo productInfo;
  private Long couponInfoId;
  private PaymentInfo paymentInfo;
  private String orderNo;
  private Long memberId;
  private int refundPoints;

  public static RefundDTO of(OrderDetail orderDetail, Refund refund) {

    ProductInfo productInfo =
        ProductInfo.builder()
            .productId(orderDetail.getProductId())
            .quantity(orderDetail.getProductQuantity().longValue())
            .sizeId(orderDetail.getProductSizeId())
            .build();
    PaymentInfo paymentInfo =
        PaymentInfo.builder()
            .orderNo(orderDetail.getOrderNo())
            .cancelAmount(refund.getPrice())
            .build();
    return RefundDTO.builder()
        .productInfo(productInfo)
        .paymentInfo(paymentInfo)
        .orderNo(orderDetail.getOrderNo())
        .couponInfoId(orderDetail.getCouponInfoId())
        .memberId(orderDetail.getOrder().getMemberId())
        .build();
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ProductInfo {
    private Long productId;
    private Long sizeId;
    private Long quantity;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class PaymentInfo {
    private String orderNo;
    private int cancelAmount;
  }
}
