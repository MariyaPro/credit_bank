package com.prokofeva.deal_api.service;

import com.prokofeva.dto.ClientDto;
import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanStatementRequestDto;

public interface ClientService {
    ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto, String logId);

    ClientDto updateClientInfo(ClientDto clientDto, FinishRegistrationRequestDto finRegRequestDto, String statementId);
}
