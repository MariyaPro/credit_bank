package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;

public interface StatementService {
    void setAppliedOffer(LoanOfferDto loanOfferDto);
}
