package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.dto.LoanOfferDto;
import com.prokofeva.deal_api.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto);

    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId);

    void sendDocuments(String statementId);

    void signDocuments(String statementId);

    void codeDocuments(String statementId);
}
