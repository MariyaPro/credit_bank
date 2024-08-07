package com.prokofeva.deal_api.service;

import com.prokofeva.dto.ClientDto;
import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.StatementDto;
import com.prokofeva.enums.ApplicationStatus;

import java.util.List;

public interface StatementService {
    void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId);

    StatementDto createStatement(ClientDto clientDto, String logId);

    StatementDto getStatementById(String statementId, String logId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb, String logId);

    void updateStatementStatus(ApplicationStatus status, String statementId, String logId);

    boolean checkSesCode(String sesCode, String statementId, String logId);

    void setupSesCode(String statementId, String logId);

    List<StatementDto> getListStatements(String logId);
}

