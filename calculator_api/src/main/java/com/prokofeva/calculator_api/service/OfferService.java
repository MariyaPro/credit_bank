package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;

public interface OfferService {
    LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                             boolean isInsuranceEnabled, boolean isSalaryClient);
}
