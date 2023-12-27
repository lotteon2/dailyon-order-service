package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterDeliveryRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterItemRequest;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.PaymentDTO.PaymentReadyParam;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductParam;
import com.dailyon.orderservice.domain.torder.entity.TOrder;
import com.dailyon.orderservice.domain.torder.entity.TOrderDetail;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.OrderDTO;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.OrderDTO.PaymentInfo;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.OrderDTO.ProductInfo;
import com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterDelivery;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterOrderItem;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterTOrder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.dailyon.orderservice.domain.torder.kafka.event.dto.enums.OrderEvent.PENDING;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    imports = {OrderEvent.class})
public interface TOrderDtoMapper {

  CouponParam toCouponParam(RegisterItemRequest request);

  OrderProductParam toOrderProductParam(RegisterItemRequest request);

  @Mapping(target = "registerDelivery", source = "request.deliveryInfo")
  @Mapping(target = "couponInfoMap", source = "productCoupons")
  @Mapping(target = "productInfoMap", source = "request")
  RegisterTOrder of(
      TOrderCreateRequest request,
      List<ProductCouponDTO> productCoupons,
      List<OrderProductDTO> orderProductDTOList);

  default Map<Long, ProductCouponDTO> toCouponInfoMap(List<ProductCouponDTO> productCoupons) {
    return productCoupons.stream()
        .collect(toMap(ProductCouponDTO::getProductId, Function.identity()));
  }

  default Map<Long, RegisterOrderItem> toProductInfoMap(TOrderCreateRequest request) {
    return request.getOrderItems().stream()
        .collect(toMap(RegisterItemRequest::getProductId, this::toRegisterOrderItem));
  }

  @Mapping(target = "orderId", source = "order.id")
  @Mapping(target = "method", source = "method")
  @Mapping(target = "productName", expression = "java(getProductName(order))")
  @Mapping(target = "deliveryFee", source = "order.deliveryFee")
  @Mapping(target = "quantity", expression = "java(order.getOrderDetails().size())")
  @Mapping(target = "totalAmount", expression = "java(order.getTotalAmount().intValue())")
  @Mapping(
      target = "totalCouponDiscountPrice",
      expression = "java(order.calculateTotalCouponDiscountPrice())")
  @Mapping(target = "usedPoints", source = "usedPoints")
  PaymentReadyParam toPaymentReadyParam(TOrder order, String method, Integer usedPoints);

  default String getProductName(TOrder order) {
    return order.getOrderDetails().size() == 1
        ? order.getOrderDetails().get(0).getProductName()
        : order.getOrderDetails().get(0).getProductName()
            + "외 "
            + (order.getOrderDetails().size() - 1)
            + "항목";
  }

  default OrderDTO of(TOrder tOrder, String pgToken) {
    List<TOrderDetail> details = tOrder.getOrderDetails();
    List<ProductInfo> productInfo =
        details.stream()
            .map(
                tOrderDetail ->
                    ProductInfo.builder()
                        .productId(tOrderDetail.getProductId())
                        .sizeId(tOrderDetail.getProductSizeId())
                        .quantity(tOrderDetail.getProductQuantity().longValue())
                        .build())
            .collect(Collectors.toUnmodifiableList());

    List<Long> couponInfo =
        details.stream()
            .map(TOrderDetail::getCouponInfoId)
            .filter(Objects::nonNull)
            .collect(toUnmodifiableList());

    PaymentInfo paymentInfo = PaymentInfo.builder().pgToken(pgToken).build();

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

  default RegisterOrderItem toRegisterOrderItem(RegisterItemRequest item) {
    return RegisterOrderItem.builder()
        .productId(item.getProductId())
        .sizeId(item.getSizeId())
        .quantity(item.getQuantity())
        .referralCode(item.getReferralCode())
        .build();
  }

  default RegisterDelivery toRegisterDelivery(RegisterDeliveryRequest delivery) {
    return RegisterDelivery.builder()
        .receiver(delivery.getReceiver())
        .postCode(delivery.getPostCode())
        .roadAddress(delivery.getRoadAddress())
        .detailAddress(delivery.getDetailAddress())
        .phoneNumber(delivery.getPhoneNumber())
        .build();
  }

  default List<CouponParam> toCouponParams(List<RegisterItemRequest> orderItems) {
    return orderItems.stream()
        .filter(registerItemRequest -> registerItemRequest.getCouponInfoId() != null)
        .map(this::toCouponParam)
        .collect(toUnmodifiableList());
  }

  default List<OrderProductParam> toOrderProductParams(List<RegisterItemRequest> orderItems) {
    return orderItems.stream().map(this::toOrderProductParam).collect(toUnmodifiableList());
  }
}
