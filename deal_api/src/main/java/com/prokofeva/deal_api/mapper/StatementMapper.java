package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.StatementDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {ClientMapper.class, CreditMapper.class})
public interface StatementMapper {
    StatementDto convertEntityToDto(Statement statement);

    Statement convertDtoToEntity(StatementDto statementDto);
}
