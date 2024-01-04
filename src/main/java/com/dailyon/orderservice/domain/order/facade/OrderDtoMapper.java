package com.dailyon.orderservice.domain.order.facade;

import com.dailyon.orderservice.domain.order.api.request.GiftDto;
import com.dailyon.orderservice.domain.order.service.request.GiftCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrderDtoMapper {

    GiftCommand.Accept from(GiftDto.createDelivery request);
}
