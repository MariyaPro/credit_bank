package com.prokofeva.statement_api.service;

import com.prokofeva.statement_api.doman.LoanOfferDto;
import com.prokofeva.statement_api.doman.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto);

    void selectAppliedOffer(LoanOfferDto loanOfferDto);
}