package com.dailyon.orderservice.domain.torder.api.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.CouponInfo;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.*;

public class TOrderRequest {
  @Getter
  @NoArgsConstructor
  public class TOrderCreateRequest {
    @Valid private List<OrderItem> orderItems;

    @PositiveOrZero(message = "포인트는 0이상 이어야 합니다.")
    private Integer memberPoints;

    @NotBlank(message = "주문 타입은 필수 입니다.")
    private OrderType type;

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
          .type(type)
          .memberPoints(memberPoints)
          .couponInfos(couponInfos)
          .orderProductInfos(orderProductInfos)
          .build();
    }

    @Getter
    @NoArgsConstructor
    public class OrderItem {

      @NotNull(message = "상품 아이디는 필수 입니다.")
      private Long productId;

      @NotNull(message = "카테고리 아이디는 필수 입니다.")
      private Long categoryId;

      @NotNull(message = "치수는 필수 입니다.")
      private Long sizeId;

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
  }
}
