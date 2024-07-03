package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;

public interface OfferService {
    LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                             boolean isInsuranceEnabled, boolean isSalaryClient, String logId);
}
