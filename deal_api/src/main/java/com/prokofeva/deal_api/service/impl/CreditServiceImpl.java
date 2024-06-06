package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.service.CreditService;
import com.prokofeva.deal_api.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final StatementService statementService;

    @Override
    public void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   String statementId) {
       // UUID clientId = statementService.findClientIdByStatementId(statementId);

    }
}
