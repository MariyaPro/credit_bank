package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;

import java.util.List;

public interface OfferService {
    List<LoanOfferDto> gelListOffers (LoanStatementRequestDto loanStatementRequestDto);
}
