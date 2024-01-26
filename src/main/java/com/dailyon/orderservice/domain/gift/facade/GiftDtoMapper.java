package com.dailyon.orderservice.domain.gift.facade;

import com.dailyon.orderservice.domain.gift.api.request.GiftDto;
import com.dailyon.orderservice.domain.gift.service.request.GiftCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface GiftDtoMapper {

  GiftCommand.Accept from(GiftDto.createDelivery request);
}
