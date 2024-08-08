package com.prokofeva.calculator_api.service;

import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;

public interface OfferService {
    LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                             boolean isInsuranceEnabled, boolean isSalaryClient, String logId);
}