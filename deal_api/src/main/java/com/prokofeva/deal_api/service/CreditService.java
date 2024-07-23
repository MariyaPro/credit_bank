package com.prokofeva.deal_api.service;

import com.prokofeva.dto.CreditDto;
import com.prokofeva.enums.CreditStatus;

public interface CreditService {
    CreditDto createCredit(CreditDto creditDto, String statementId);

    void updateCreditStatus(CreditStatus creditStatus, CreditDto creditDto, String logId);
}
