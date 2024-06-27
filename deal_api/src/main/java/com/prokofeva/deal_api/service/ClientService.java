package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.dto.ClientDto;
import com.prokofeva.deal_api.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.dto.LoanStatementRequestDto;

public interface ClientService {
    ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto, String logId);

    ClientDto updateClientInfo(ClientDto clientDto, FinishRegistrationRequestDto finRegRequestDto, String statementId);
}
