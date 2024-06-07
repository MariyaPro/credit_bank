package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING, uses = {PassportMapper.class, EmploymentMapper.class})
public interface ClientMapper {
    ClientDto convertEntityToDto(Client client);

    Client convertDtoToEntity(ClientDto clientDto);
}
