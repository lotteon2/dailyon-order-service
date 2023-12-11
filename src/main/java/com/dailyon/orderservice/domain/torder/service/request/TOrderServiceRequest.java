package com.dailyon.orderservice.domain.torder.service.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateDiscountPrice;
import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateOrderPrice;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateCouponInfo;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateStock;

@Getter
@NoArgsConstructor
public class TOrderServiceRequest {

  private List<OrderProductDTO> orderProductDTOList;
  private Map<Long, OrderProductInfo> productInfoMap;
  private Map<Long, ProductCouponDTO> couponInfoMap;
  private OrderInfo orderInfo;

  @Builder
  private TOrderServiceRequest(
      List<OrderProductDTO> orderProductDTOList,
      Map<Long, OrderProductInfo> productInfoMap,
      Map<Long, ProductCouponDTO> couponInfoMap,
      OrderInfo orderInfo) {
    this.orderProductDTOList = orderProductDTOList;
    this.productInfoMap = productInfoMap;
    this.couponInfoMap = couponInfoMap;
    this.orderInfo = orderInfo;
  }

  public static TOrderServiceRequest of(
      List<OrderProductDTO> orderProductDTOList,
      Map<Long, OrderProductInfo> productInfoMap,
      Map<Long, ProductCouponDTO> couponInfoMap,
      OrderInfo orderInfo) {
    return TOrderServiceRequest.builder()
        .orderProductDTOList(orderProductDTOList)
        .productInfoMap(productInfoMap)
        .couponInfoMap(couponInfoMap)
        .orderInfo(orderInfo)
        .build();
  }

  public TOrder createOrder(String orderId, Long memberId) {
    List<TOrderDetail> orderDetails =
        orderProductDTOList.stream()
            .map(
                orderProduct -> {
                  Optional<ProductCouponDTO> couponOptional =
                      Optional.ofNullable(couponInfoMap.get(orderProduct.getProductId()));
                  OrderProductInfo orderProductInfo =
                      productInfoMap.get(orderProduct.getProductId());

                  validateStock(orderProductInfo.getQuantity(), orderProduct.getStock());

                  int discountPrice =
                      couponOptional.isPresent()
                          ? calculateDiscountPrice(
                              orderProduct, couponOptional.get(), orderProductInfo.getQuantity())
                          : 0;

                  validateCouponInfo(
                      orderProduct.getPrice() * orderProductInfo.getQuantity(), couponOptional);
                  int orderPrice =
                      calculateOrderPrice(orderProduct, orderProductInfo, discountPrice);

                  return createTOrderDetail(
                      orderId,
                      orderProduct,
                      couponOptional.orElse(null),
                      orderProductInfo,
                      discountPrice,
                      orderPrice);
                })
            .collect(Collectors.toUnmodifiableList());

    return TOrder.builder()
        .id(orderId)
        .memberId(memberId)
        .deliveryFee(orderInfo.getDeliveryFee())
        .usedPoints(orderInfo.getUsedPoints())
        .totalCouponDiscountPrice(orderInfo.getTotalCouponDiscountPrice())
        .type(orderInfo.getType().name())
        .orderDetails(orderDetails)
        .build();
  }

  private TOrderDetail createTOrderDetail(
      String orderNo,
      OrderProductDTO orderProduct,
      ProductCouponDTO coupon,
      OrderProductInfo orderProductInfo,
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
            .productQuantity(orderProductInfo.getQuantity())
            .productSize(orderProduct.getSizeName())
            .productSizeId(orderProduct.getSizeId());

    if (coupon != null) {
      builder
          .couponInfoId(coupon.getProductId())
          .couponDiscountPrice(discountedPrice)
          .couponName(coupon.getCouponName());
    }
    return builder.build();
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
