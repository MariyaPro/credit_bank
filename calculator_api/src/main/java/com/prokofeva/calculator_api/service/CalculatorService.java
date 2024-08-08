package com.prokofeva.calculator_api.service;

import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId);
}
