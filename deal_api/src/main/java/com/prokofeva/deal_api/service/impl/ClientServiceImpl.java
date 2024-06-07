package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.mapper.ClientMapper;
import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepo clientRepo;
    private final ClientMapper clientMapper;

    @Override
    public ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto) {
        Client client = new Client();
        client.setLastName(loanStatementRequestDto.getLastName());
        client.setFirstName(loanStatementRequestDto.getFirstName());
        client.setMiddleName(loanStatementRequestDto.getMiddleName());
        client.setBirthDate(loanStatementRequestDto.getBirthdate());
        client.setEmail(loanStatementRequestDto.getEmail());

        Passport passport = new Passport();
        passport.setSeries(loanStatementRequestDto.getPassportSeries());
        passport.setNumber(loanStatementRequestDto.getPassportNumber());

        client.setPassport(passport);

        return saveClient(client);
    }

    @Override
    public ClientDto saveClient(Client client) {
        return clientMapper.convertEntityToDto(clientRepo.save(client));
    }
}
