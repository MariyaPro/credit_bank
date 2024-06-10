package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;

import java.util.List;

public interface DealService {
    List<LoanOfferDto> getListOffers(LoanStatementRequestDto loanStatementRequestDto);

    void setAppliedOffer(LoanOfferDto loanOfferDto);

    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId);
}
