package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.dto.CreditDto;
import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.dto.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId);
}
