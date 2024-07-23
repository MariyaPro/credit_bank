package com.prokofeva.deal_api.service;

import com.prokofeva.dto.ClientDto;
import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.StatementDto;
import com.prokofeva.enums.ApplicationStatus;

public interface StatementService {
    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    StatementDto createStatement(ClientDto clientDto, String logId);

    StatementDto getStatementById(String statementId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb);

    void updateStatementStatus(ApplicationStatus status, String statementId, String logId);

    boolean checkSesCode(String sesCode, String statementId, String logId);

    void setupSesCode(String statementId, String logId);
}

