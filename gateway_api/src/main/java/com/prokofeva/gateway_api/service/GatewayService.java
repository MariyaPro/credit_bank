package com.prokofeva.gateway_api.service;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;

import java.util.List;

public interface GatewayService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId);

    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId, String logId);
}
