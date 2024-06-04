package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.service.OfferService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    @Override
    public List<LoanOfferDto> gelListOffers (LoanStatementRequestDto loanStatementRequestDto){
        return List.of();
    }
}
