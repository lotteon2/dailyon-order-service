package com.dailyon.orderservice.domain.torder.service.request;

import com.dailyon.orderservice.domain.order.entity.enums.OrderType;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.facade.request.TOrderFacadeRequest.TOrderFacadeCreateRequest.OrderProductInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateDiscountPrice;
import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateOrderPrice;

@Getter
@NoArgsConstructor
public class TOrderServiceRequest {

  private List<OrderProductDTO> orderProductDTOList;
  private Map<Long, OrderProductInfo> productInfoMap;
  private Map<Long, ProductCouponDTO> couponInfoMap;
  private OrderType type;

  @Builder
  private TOrderServiceRequest(
      List<OrderProductDTO> orderProductDTOList,
      Map<Long, OrderProductInfo> productInfoMap,
      Map<Long, ProductCouponDTO> couponInfoMap,
      OrderType type) {
    this.orderProductDTOList = orderProductDTOList;
    this.productInfoMap = productInfoMap;
    this.couponInfoMap = couponInfoMap;
    this.type = type;
  }

  public static TOrderServiceRequest of(
      List<OrderProductDTO> orderProductDTOList,
      Map<Long, OrderProductInfo> productInfoMap,
      Map<Long, ProductCouponDTO> couponInfoMap,
      OrderType type) {
    return TOrderServiceRequest.builder()
        .orderProductDTOList(orderProductDTOList)
        .productInfoMap(productInfoMap)
        .couponInfoMap(couponInfoMap)
        .type(type)
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

                  int discountPrice =
                      couponOptional.isPresent()
                          ? calculateDiscountPrice(
                              orderProduct, couponOptional.get(), orderProductInfo.getQuantity())
                          : 0;

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
        .type(type)
        .orderDetails(orderDetails)
        .build();
  }

  private TOrderDetail createTOrderDetail(
      String orderId,
      OrderProductDTO orderProduct,
      ProductCouponDTO coupon,
      OrderProductInfo orderProductInfo,
      int discountedPrice,
      int orderPrice) {
    TOrderDetail.TOrderDetailBuilder builder =
        TOrderDetail.builder()
            .orderPrice(orderPrice)
            .productId(orderProduct.getProductId())
            .orderId(orderId)
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
}
