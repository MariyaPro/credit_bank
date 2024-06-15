package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;

public interface ClientService {
    ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto);

    ClientDto updateClientInfo(ClientDto clientDto, FinishRegistrationRequestDto finRegRequestDto);
}
