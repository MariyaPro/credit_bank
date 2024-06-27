package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.model.dto.LoanOfferDto;
import com.prokofeva.calculator_api.model.dto.LoanStatementRequestDto;

public interface OfferService {
    LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                             boolean isInsuranceEnabled, boolean isSalaryClient, String logId);
}
