package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.model.dto.CreditDto;
import com.prokofeva.calculator_api.model.dto.LoanOfferDto;
import com.prokofeva.calculator_api.model.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.model.dto.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId);
}
