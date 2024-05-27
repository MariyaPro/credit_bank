package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;

import java.util.List;

public interface LoanService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto);
}
