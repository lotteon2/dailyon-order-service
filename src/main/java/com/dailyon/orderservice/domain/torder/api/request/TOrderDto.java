package com.dailyon.orderservice.domain.torder.api.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

public class TOrderDto {
  @Getter
  @Setter
  @ToString
  public static class TOrderCreateRequest {
    @PositiveOrZero(message = "포인트는 0이상 이어야 합니다.")
    private int usedPoints;

    @NotNull(message = "주문 타입은 필수 입니다.")
    private OrderType type;

    @PositiveOrZero(message = "배송비는 0이상 이어야 합니다.")
    private int deliveryFee;

    private int totalCouponDiscountPrice;

    @Valid private List<RegisterItemRequest> orderItems;
    @Valid private RegisterDeliveryRequest deliveryInfo;

    @NotBlank(message = "결제 수단은 필수 입니다.")
    private String paymentType;

    private Long receiverId;
    private String receiverName;
    private String senderName;

    private String referralCode;
    private String auctionId;

    @Getter
    @Setter
    @ToString
    public static class RegisterItemRequest {

      @NotNull(message = "상품 아이디는 필수 입니다.")
      private Long productId;

      private Long categoryId;

      @NotNull(message = "치수는 필수 입니다.")
      private Long sizeId;

      @NotNull(message = "주문 가격은 필수 입니다.")
      @PositiveOrZero(message = "주문 가격은 0원 이상이어야 합니다.")
      private Integer orderPrice;

      @Positive(message = "상품 수량은 0개일 수 없습니다.")
      private Integer quantity;

      private Long couponInfoId;
    }

    @Getter
    @Setter
    @ToString
    public static class RegisterDeliveryRequest {
      @NotEmpty(message = "수령인은 필수 입니다.")
      private String receiver;

      @NotEmpty(message = "우편번호는 필수 입니다.")
      private String postCode;

      @NotEmpty(message = "도로명 주소는 필수 입니다.")
      private String roadAddress;

      @NotEmpty(message = "상세 주소는 필수 입니다.")
      private String detailAddress;

      private String phoneNumber;
    }
  }

  @Getter
  @Setter
  @ToString
  public static class OrderApproveRequest {
    @NotEmpty(message = "pgToken은 필수 입니다.")
    private String pg_token;
  }
}
