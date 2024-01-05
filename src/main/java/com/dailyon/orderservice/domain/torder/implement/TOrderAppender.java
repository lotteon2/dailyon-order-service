package com.dailyon.orderservice.domain.torder.implement;

import com.dailyon.orderservice.domain.torder.entity.TDelivery;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.repository.OrderDynamoRepository;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterTOrder;
import dailyon.domain.order.clients.CouponDTO.ProductCouponDTO;
import dailyon.domain.order.clients.ProductDTO.OrderProductListDTO.OrderProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateDiscountPrice;
import static com.dailyon.orderservice.common.utils.OrderCalculator.calculateOrderPrice;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateCouponInfo;
import static com.dailyon.orderservice.common.utils.OrderValidator.validateStock;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Component
public class TOrderAppender {
  private final OrderDynamoRepository orderDynamoRepository;

  public TOrder append(
      RegisterTOrder requestOrder, String orderNo, Long memberId, TDelivery tDelivery) {
    TOrder order = createOrder(requestOrder, orderNo, memberId, tDelivery);
    return orderDynamoRepository.save(order);
  }

  private TOrder createOrder(
      RegisterTOrder requestOrder, String orderNo, Long memberId, TDelivery tDelivery) {
    List<TOrderDetail> orderDetails = createOrderDetails(requestOrder, orderNo);
    Long totalAmount = calculateTotalAmount(orderDetails, requestOrder.getUsedPoints());
    return buildOrder(requestOrder, orderNo, memberId, orderDetails, totalAmount, tDelivery);
  }

  private List<TOrderDetail> createOrderDetails(RegisterTOrder requestOrder, String orderNo) {
    return requestOrder.getOrderProductDTOList().stream()
        .map(orderProduct -> createEachOrderDetail(requestOrder, orderNo, orderProduct))
        .collect(Collectors.toUnmodifiableList());
  }

  private TOrderDetail createEachOrderDetail(
      RegisterTOrder requestOrder, String orderNo, OrderProductDTO orderProduct) {
    var couponInfoMap = requestOrder.getCouponInfoMap();
    var productInfoMap = requestOrder.getProductInfoMap();
    var couponOptional = ofNullable(couponInfoMap.get(orderProduct.getProductId()));
    var orderItem = productInfoMap.get(orderProduct.getProductId());

    validateStock(orderItem.getQuantity(), orderProduct.getStock());

    int discountPrice =
        calculateDiscountPrice(orderProduct, couponOptional.orElse(null), orderItem.getQuantity());
    validateCouponInfo(orderProduct.getPrice() * orderItem.getQuantity(), couponOptional);

    int orderPrice = calculateOrderPrice(orderProduct, orderItem, discountPrice);

    return createTOrderDetail(
        orderNo, orderProduct, couponOptional.orElse(null), orderItem, discountPrice, orderPrice);
  }

  private TOrder buildOrder(
      RegisterTOrder requestOrder,
      String orderNo,
      Long memberId,
      List<TOrderDetail> orderDetails,
      Long totalAmount,
      TDelivery tDelivery) {
    return TOrder.builder()
        .id(orderNo)
        .memberId(memberId)
        .deliveryFee(requestOrder.getDeliveryFee())
        .usedPoints(requestOrder.getUsedPoints())
        .totalCouponDiscountPrice(requestOrder.getTotalCouponDiscountPrice())
        .productsName(getProductsName(orderDetails))
        .totalAmount(totalAmount)
        .type(requestOrder.getType().name())
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
