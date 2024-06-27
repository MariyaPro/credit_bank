package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.dto.CreditDto;
import com.prokofeva.deal_api.enums.CreditStatus;
import com.prokofeva.deal_api.mapper.CreditMapper;
import com.prokofeva.deal_api.model.Credit;
import com.prokofeva.deal_api.repositories.CreditRepo;
import com.prokofeva.deal_api.service.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepo creditRepo;
    private final CreditMapper creditMapper;

    @Override
    public CreditDto createCredit(CreditDto creditDto, String logId) {
        Credit credit = creditMapper.convertDtoToEntity(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        log.info("{} -- Создан новый кредит: {}.",logId, credit);
        return saveCredit(credit, logId);
    }

    private CreditDto saveCredit(Credit credit, String logId) {
        Credit creditFromDb = creditRepo.saveAndFlush(credit);
        log.info("{} -- Данные о кредите успешно сохранены: {}.",logId, credit);
        return creditMapper.convertEntityToDto(creditFromDb);
    }
}
