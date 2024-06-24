package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.model.Client;
import com.prokofeva.deal_api.model.dto.ClientDto;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface ClientMapper {
    ClientDto convertEntityToDto(Client client);

    Client convertDtoToEntity(ClientDto clientDto);
}
