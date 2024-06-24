package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.model.dto.ClientDto;
import com.prokofeva.deal_api.model.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.model.dto.LoanStatementRequestDto;

public interface ClientService {
    ClientDto createClient(LoanStatementRequestDto loanStatementRequestDto, String logId);

    ClientDto updateClientInfo(ClientDto clientDto, FinishRegistrationRequestDto finRegRequestDto, String statementId);
}
