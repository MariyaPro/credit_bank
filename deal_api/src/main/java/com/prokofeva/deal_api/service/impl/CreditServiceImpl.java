package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import com.prokofeva.deal_api.doman.enums.CreditStatus;
import com.prokofeva.deal_api.mapper.CreditMapper;
import com.prokofeva.deal_api.repositories.CreditRepo;
import com.prokofeva.deal_api.service.CreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepo creditRepo;
    private final CreditMapper creditMapper;

    @Override
    public CreditDto createCredit(CreditDto creditDto) {
        Credit credit = creditMapper.convertDtoToEntity(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        return saveCredit(credit);
    }

    private CreditDto saveCredit(Credit credit) {
        return creditMapper.convertEntityToDto(creditRepo.saveAndFlush(credit));
    }
}
