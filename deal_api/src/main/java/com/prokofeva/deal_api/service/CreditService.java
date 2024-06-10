package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.dto.CreditDto;

public interface CreditService {
    CreditDto createCredit(CreditDto creditDto);

    CreditDto saveCredit(Credit credit);
}
