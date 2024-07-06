package com.prokofeva.deal_api.service;

import com.prokofeva.dto.CreditDto;

public interface CreditService {
    CreditDto createCredit(CreditDto creditDto, String statementId);

}
