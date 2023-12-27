package com.dailyon.orderservice.domain.torder.facade;

import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterDeliveryRequest;
import com.dailyon.orderservice.domain.torder.api.request.TOrderDto.TOrderCreateRequest.RegisterItemRequest;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.CouponParam;
import com.dailyon.orderservice.domain.torder.clients.dto.CouponDTO.ProductCouponDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductListDTO.OrderProductDTO;
import com.dailyon.orderservice.domain.torder.clients.dto.ProductDTO.OrderProductParam;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterDelivery;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterOrderItem;
import com.dailyon.orderservice.domain.torder.service.request.TOrderCommand.RegisterTOrder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TOrderDtoMapper {

  CouponParam toCouponParam(RegisterItemRequest request);

  OrderProductParam toOrderProductParam(RegisterItemRequest request);

  @Mapping(source = "request.deliveryInfo", target = "registerDelivery")
  @Mapping(source = "productCoupons", target = "couponInfoMap")
  @Mapping(source = "request", target = "productInfoMap")
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
