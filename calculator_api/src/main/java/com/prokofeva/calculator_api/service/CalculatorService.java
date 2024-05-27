package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;

import java.util.List;

public interface CalculatorService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto);

    CreditDto calculateCredit(ScoringDataDto scoringDataDto);
}
