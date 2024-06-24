package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.model.dto.ClientDto;
import com.prokofeva.deal_api.model.dto.CreditDto;
import com.prokofeva.deal_api.model.dto.LoanOfferDto;
import com.prokofeva.deal_api.model.dto.StatementDto;

public interface StatementService {
    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    StatementDto createStatement(ClientDto clientDto, String logId);

    StatementDto getStatementById(String statementId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb);
}

