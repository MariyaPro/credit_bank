package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;

import java.util.UUID;

public interface ClientService {
    ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto);

    ClientDto saveClient(Client client);
}
