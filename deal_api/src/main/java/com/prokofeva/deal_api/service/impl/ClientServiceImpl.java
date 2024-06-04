package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.service.ClientService;
import com.prokofeva.deal_api.service.mapper.ClientMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;

    public ClientDto saveClient(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = new Client();
        client.setLastName(loanStatementRequestDto.getLastName());
        client.setFirstName(loanStatementRequestDto.getFirstName());
        client.setMiddleName(loanStatementRequestDto.getMiddleName());
        client.setBirthDate(loanStatementRequestDto.getBirthdate());
        client.setEmail(loanStatementRequestDto.getEmail());

        client.setPassportId(new Passport(
                loanStatementRequestDto.getPassportSeries(),
                loanStatementRequestDto.getPassportNumber()
        ));

        return ClientMapper.convertEntityToDto(clientRepo.save(client));
    }
}
