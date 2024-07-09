package com.prokofeva.deal_api.service;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.StatementDto;
import com.prokofeva.enums.ApplicationStatus;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto);

    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId);

    StatementDto getStatement(String statementId, String logId);

    void updateStatementStatus(ApplicationStatus applicationStatus, String statementId, String logId);

    void checkSesCode(String sesCode, String statementId, String logId);

    void signDocuments(String statementId, String logId);
}
