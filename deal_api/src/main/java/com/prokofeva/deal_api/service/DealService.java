package com.prokofeva.deal_api.service;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto);

    void selectAppliedOffer(LoanOfferDto loanOfferDto);

    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId);
}
