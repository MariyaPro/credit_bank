package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface CreditMapper {
    CreditDto convertEntityToDto(Credit credit);

    Credit convertDtoToEntity(CreditDto creditDto);
}