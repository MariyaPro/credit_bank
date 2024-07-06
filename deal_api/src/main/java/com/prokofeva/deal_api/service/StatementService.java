package com.prokofeva.deal_api.service;

import com.prokofeva.dto.ClientDto;
import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.StatementDto;

public interface StatementService {
    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    StatementDto createStatement(ClientDto clientDto, String logId);

    StatementDto getStatementById(String statementId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb);
}

