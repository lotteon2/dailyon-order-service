package com.dailyon.orderservice.domain.torder.kafka.event.dto;

import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent.PENDING;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

  private List<ProductInfo> productInfos;
  private List<Long> couponInfos;
  private PaymentInfo paymentInfo;
  private String orderNo;
  private Long memberId;
  private int usedPoints;
  private OrderEvent orderEvent;

  public static OrderDTO of(TOrder tOrder, String pgToken) {
    List<ProductInfo> productInfo = createProductInfo(tOrder.getOrderDetails());
    List<Long> couponInfo = createCouponInfo(tOrder.getOrderDetails());
    PaymentInfo paymentInfo = createPaymentInfo(pgToken);
    return OrderDTO.builder()
        .productInfos(productInfo)
        .couponInfos(couponInfo)
        .paymentInfo(paymentInfo)
        .orderNo(tOrder.getId())
        .memberId(tOrder.getMemberId())
        .usedPoints(tOrder.getUsedPoints())
        .orderEvent(PENDING)
        .build();
  }

  private static List<ProductInfo> createProductInfo(List<TOrderDetail> details) {
    return details.stream()
        .map(
            tOrderDetail ->
                ProductInfo.builder()
                    .productId(tOrderDetail.getProductId())
                    .sizeId(tOrderDetail.getProductSizeId())
                    .quantity(tOrderDetail.getProductQuantity().longValue())
                    .build())
        .collect(Collectors.toUnmodifiableList());
  }

  private static List<Long> createCouponInfo(List<TOrderDetail> details) {
    return details.stream()
        .map(TOrderDetail::getCouponInfoId)
        .filter(Objects::nonNull)
        .collect(Collectors.toUnmodifiableList());
  }

  private static PaymentInfo createPaymentInfo(String pgToken) {
    return PaymentInfo.builder().pgToken(pgToken).build();
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
    private String pgToken;
  }
}
