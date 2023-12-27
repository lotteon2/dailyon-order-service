package com.dailyon.orderservice.domain.torder.service.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.entity.TDelivery;
import lombok.*;

import java.util.List;
import java.util.Map;

public class TOrderCommand {

  @Getter
  @Builder
  @ToString
  public static class RegisterTOrder {
    private final int usedPoints;
    private final OrderType type;
    private final int deliveryFee;
    private final int totalCouponDiscountPrice;
    private final List<OrderProductDTO> orderProductDTOList;
    private final RegisterDelivery registerDelivery;
    private final Map<Long, ProductCouponDTO> couponInfoMap;
    private final Map<Long, RegisterOrderItem> productInfoMap;
    private String paymentType;

    public TDelivery createTDelivery(String orderNo) {
      return registerDelivery != null ? registerDelivery.toEntity(orderNo) : null;
    }
  }

  @Getter
  @Builder
  @ToString
  public static class RegisterOrderItem {
    private Long productId;
    private Long sizeId;
    private Integer quantity;
    private String referralCode;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class RegisterDelivery {
    private String receiver;
    private String postCode;
    private String roadAddress;
    private String detailAddress;
    private String phoneNumber;

    public TDelivery toEntity(String orderNo) {
      return TDelivery.builder()
          .orderNo(orderNo)
          .receiver(receiver)
          .postCode(postCode)
          .roadAddress(roadAddress)
          .detailAddress(detailAddress)
          .phoneNumber(phoneNumber)
          .build();
    }
  }
}
