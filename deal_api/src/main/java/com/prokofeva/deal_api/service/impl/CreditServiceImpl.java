package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import com.prokofeva.deal_api.doman.enums.CreditStatus;
import com.prokofeva.deal_api.mapper.CreditMapper;
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
    public CreditDto createCredit(CreditDto creditDto) {
        Credit credit = creditMapper.convertDtoToEntity(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        log.info("Создан новый кредит: {}.", credit);
        return saveCredit(credit);
    }

    private CreditDto saveCredit(Credit credit) {
        Credit creditFromDb = creditRepo.saveAndFlush(credit);
        log.info("Данные о кредите успешно сохранены: {}.", credit);
        return creditMapper.convertEntityToDto(creditFromDb);
    }
}
