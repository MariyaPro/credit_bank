package com.prokofeva.deal_api.service;

import com.prokofeva.deal_api.doman.dto.FinishRegistrationRequestDto;

public interface CreditService {
    void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto,
                            String statementId);
}
