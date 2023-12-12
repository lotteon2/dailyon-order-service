package com.dailyon.orderservice.domain.torder.api.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeApproveRequest;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.CouponInfo;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

public class TOrderRequest {
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TOrderCreateRequest {
    @Valid private List<OrderItem> orderItems;
    @Valid private OrderInfo orderInfo;
    @Valid private DeliveryInfo deliveryInfo;

    @NotBlank(message = "결제 수단은 필수 입니다.")
    private String paymentType;

    public TOrderFacadeCreateRequest toOrderFacadeCreateRequest() {
      List<CouponInfo> couponInfos =
          orderItems.stream()
              .filter(orderItem -> orderItem.getCouponInfoId() != null)
              .collect(
                  collectingAndThen(
                      mapping(OrderItem::toCouponInfo, toList()), Collections::unmodifiableList));
      List<OrderProductInfo> orderProductInfos =
          orderItems.stream()
              .collect(
                  collectingAndThen(
                      mapping(OrderItem::toOrderProductInfo, toList()),
                      Collections::unmodifiableList));

      return TOrderFacadeCreateRequest.builder()
          .couponInfos(couponInfos)
          .orderProductInfos(orderProductInfos)
          .orderInfo(orderInfo.toFacadeOrderInfo())
          .deliveryInfo(deliveryInfo.toFacadeDeliveryInfo())
          .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {

      @NotNull(message = "상품 아이디는 필수 입니다.")
      private Long productId;

      @NotNull(message = "카테고리 아이디는 필수 입니다.")
      private Long categoryId;

      @NotNull(message = "치수는 필수 입니다.")
      private Long sizeId;

      @NotNull(message = "주문 가격은 필수 입니다.")
      @PositiveOrZero(message = "주문 가격은 0원 이상이어야 합니다.")
      private Integer orderPrice;

      @Positive(message = "상품 수량은 0개일 수 없습니다.")
      private Integer quantity;

      private Long couponInfoId;

      private String referralCode;

      public CouponInfo toCouponInfo() {
        return CouponInfo.builder()
            .productId(productId)
            .categoryId(categoryId)
            .couponInfoId(couponInfoId)
            .build();
      }

      public OrderProductInfo toOrderProductInfo() {
        return OrderProductInfo.builder()
            .productId(productId)
            .sizeId(sizeId)
            .quantity(quantity)
            .referralCode(referralCode)
            .build();
      }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderInfo {
      @PositiveOrZero(message = "포인트는 0이상 이어야 합니다.")
      private int usedPoints;

      @NotNull(message = "주문 타입은 필수 입니다.")
      private OrderType type;

      @PositiveOrZero(message = "배송비는 0이상 이어야 합니다.")
      private int deliveryFee;

      private int totalCouponDiscountPrice;

      public TOrderFacadeCreateRequest.OrderInfo toFacadeOrderInfo() {
        return TOrderFacadeCreateRequest.OrderInfo.builder()
            .deliveryFee(deliveryFee)
            .usedPoints(usedPoints)
            .type(type)
            .totalCouponDiscountPrice(totalCouponDiscountPrice)
            .build();
      }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeliveryInfo {
      @NotEmpty(message = "수령인은 필수 입니다.")
      private String receiver;

      @NotEmpty(message = "우편번호는 필수 입니다.")
      private String postCode;

      @NotEmpty(message = "도로명 주소는 필수 입니다.")
      private String roadAddress;

      @NotEmpty(message = "상세 주소는 필수 입니다.")
      private String detailAddress;

      private String phoneNumber;

      public TOrderFacadeCreateRequest.DeliveryInfo toFacadeDeliveryInfo() {
        return TOrderFacadeCreateRequest.DeliveryInfo.builder()
            .receiver(receiver)
            .postCode(postCode)
            .roadAddress(roadAddress)
            .detailAddress(detailAddress)
            .phoneNumber(phoneNumber)
            .build();
      }
    }
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class OrderApproveRequest {
    @NotEmpty(message = "pgToken은 필수 입니다.")
    private String pg_token;

    public TOrderFacadeApproveRequest toFacadeRequest(String orderId) {
      return TOrderFacadeApproveRequest.builder().orderId(orderId).pgToken(pg_token).build();
    }
  }
}
