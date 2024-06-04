package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;

import java.util.UUID;

public interface StatementService {
    void setAppliedOffer(LoanOfferDto loanOfferDto);
    UUID findClientIdByStatementId(String statementId);
    StatementDto saveStatement (ClientDto clientDto);
}
