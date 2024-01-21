package com.dailyon.orderservice.domain.torder.service.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.entity.TDelivery;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateDiscountPrice;
import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateOrderPrice;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateCouponInfo;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateStock;
import static java.util.Optional.ofNullable;

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
    private String referralCode;
    private Long orderPrice;
    private String auctionId;

    public TDelivery createTDelivery(String orderNo) {
      return registerDelivery != null ? registerDelivery.toEntity(orderNo) : null;
    }

    public TOrder createOrder(String orderNo, Long memberId, TDelivery tDelivery) {
      List<TOrderDetail> orderDetails = createOrderDetails(orderNo);
      Long totalAmount =
          type.equals(OrderType.AUCTION)
              ? orderPrice - usedPoints
              : calculateTotalAmount(orderDetails, usedPoints);

      return buildOrder(orderNo, memberId, orderDetails, totalAmount, tDelivery);
    }

    private List<TOrderDetail> createOrderDetails(String orderNo) {
      return orderProductDTOList.stream()
          .map(orderProduct -> createEachOrderDetail(orderNo, orderProduct))
          .collect(Collectors.toUnmodifiableList());
    }

    private TOrderDetail createEachOrderDetail(String orderNo, OrderProductDTO orderProduct) {
      var couponOptional = ofNullable(couponInfoMap.get(orderProduct.getProductId()));
      var orderItem = productInfoMap.get(orderProduct.getProductId());
      // orderItem null
      validateStock(orderItem.getQuantity(), orderProduct.getStock());

      int discountPrice =
          calculateDiscountPrice(
              orderProduct, couponOptional.orElse(null), orderItem.getQuantity());
      validateCouponInfo(orderProduct.getPrice() * orderItem.getQuantity(), couponOptional);

      int orderPrice = calculateOrderPrice(orderProduct, orderItem, discountPrice);

      return createTOrderDetail(
          orderNo, orderProduct, couponOptional.orElse(null), orderItem, discountPrice, orderPrice);
    }

    private TOrder buildOrder(
        String orderNo,
        Long memberId,
        List<TOrderDetail> orderDetails,
        Long totalAmount,
        TDelivery tDelivery) {
      return TOrder.builder()
          .id(orderNo)
          .memberId(memberId)
          .deliveryFee(deliveryFee)
          .usedPoints(usedPoints)
          .totalCouponDiscountPrice(totalCouponDiscountPrice)
          .productsName(getProductsName(orderDetails))
          .totalAmount(totalAmount)
          .referralCode(referralCode)
          .auctionId(auctionId)
          .type(type.name())
          .orderDetails(orderDetails)
          .delivery(tDelivery)
          .build();
    }

    private TOrderDetail createTOrderDetail(
        String orderNo,
        OrderProductDTO orderProduct,
        ProductCouponDTO coupon,
        TOrderCommand.RegisterOrderItem orderItem,
        int discountedPrice,
        int orderPrice) {
      TOrderDetail.TOrderDetailBuilder builder =
          TOrderDetail.builder()
              .orderPrice(orderPrice)
              .productId(orderProduct.getProductId())
              .orderNo(orderNo)
              .productName(orderProduct.getProductName())
              .productGender(orderProduct.getGender())
              .productImgUrl(orderProduct.getImgUrl())
              .productQuantity(orderItem.getQuantity())
              .productSize(orderProduct.getSizeName())
              .productSizeId(orderProduct.getSizeId());

      if (coupon != null) {
        builder
            .couponInfoId(coupon.getCouponInfoId())
            .couponDiscountPrice(discountedPrice)
            .couponName(coupon.getCouponName());
      }
      return builder.build();
    }

    private String getProductsName(List<TOrderDetail> orderDetails) {
      return orderDetails.size() == 1
          ? orderDetails.get(0).getProductName()
          : orderDetails.get(0).getProductName() + "외 " + (orderDetails.size() - 1) + "항목";
    }

    private Long calculateTotalAmount(List<TOrderDetail> orderDetails, int usedPoints) {
      return orderDetails.stream().mapToLong(TOrderDetail::getOrderPrice).sum() - usedPoints;
    }
  }

  @Getter
  @Builder
  @ToString
  public static class RegisterOrderItem {
    private Long productId;
    private Long sizeId;
    private Integer quantity;
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
