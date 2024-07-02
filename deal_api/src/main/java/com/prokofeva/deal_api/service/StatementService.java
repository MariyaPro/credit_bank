package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.dto.ClientDto;
import com.prokofeva.deal_api.dto.CreditDto;
import com.prokofeva.deal_api.dto.LoanOfferDto;
import com.prokofeva.deal_api.dto.StatementDto;
import com.prokofeva.deal_api.enums.ApplicationStatus;
import com.prokofeva.deal_api.model.Statement;

public interface StatementService {
    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    StatementDto createStatement(ClientDto clientDto, String logId);

    StatementDto getStatementById(String statementId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb);

    void addStatusHistory(Statement statement, ApplicationStatus status, String logId);

    void addStatusHistory(String statementId, ApplicationStatus status, String logId);
}

