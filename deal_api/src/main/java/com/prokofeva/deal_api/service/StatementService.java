package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.Statement;
import com.prokofeva.deal_api.doman.dto.ClientDto;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.StatementDto;

public interface StatementService {
    void setAppliedOffer(LoanOfferDto loanOfferDto);

    StatementDto saveStatement(Statement statement);

    StatementDto createStatement(ClientDto clientDto);

    StatementDto getStatementById(String statementId);

    void registrationCredit(StatementDto statementDto, CreditDto creditDtoFromDb);
}

