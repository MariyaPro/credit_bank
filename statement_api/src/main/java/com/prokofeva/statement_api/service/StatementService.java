package com.prokofeva.statement_api.service;

import com.prokofeva.statement_api.model.LoanOfferDto;
import com.prokofeva.statement_api.model.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId);
}