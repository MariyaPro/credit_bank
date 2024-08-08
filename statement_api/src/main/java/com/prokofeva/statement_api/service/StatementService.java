package com.prokofeva.statement_api.service;

import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;

import java.util.List;

public interface StatementService {
    List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId);

    void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId);
}