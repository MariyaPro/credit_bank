package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface PassportMapper {

    PassportDto convertEntityToDto(Passport passport);

    Passport convertDtoToEntity(PassportDto passportDto);
}
