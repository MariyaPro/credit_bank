package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Employment;
import com.prokofeva.deal_api.doman.dto.EmploymentDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EmploymentMapper {
    EmploymentDto convertEntityToDto(Employment employment);

    Employment convertDtoToEntity(EmploymentDto employmentDto);
}
