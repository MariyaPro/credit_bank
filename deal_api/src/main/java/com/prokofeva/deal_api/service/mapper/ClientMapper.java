package com.prokofeva.deal_api.service.mapper;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.ClientDto;

public class ClientMapper {
    public static ClientDto convertEntityToDto (Client client){
        ClientDto clientDto = new ClientDto();
        clientDto.setClientId(client.getClientId());
        clientDto.setLastName(client.getLastName());
        clientDto.setFirstName(client.getFirstName());
        clientDto.setMiddleName(client.getMiddleName());
        clientDto.setBirthDate(client.getBirthDate());
        clientDto.setEmail(client.getEmail());
        clientDto.setGender(client.getGender());
        clientDto.setMaritalStatus(client.getMaritalStatus());
        clientDto.setDependentAmount(client.getDependentAmount());
        clientDto.setPassportId(client.getPassportId());
        clientDto.setEmploymentId(client.getEmploymentId());
        clientDto.setAccountNumber(client.getAccountNumber());

        return clientDto;
    }

}
