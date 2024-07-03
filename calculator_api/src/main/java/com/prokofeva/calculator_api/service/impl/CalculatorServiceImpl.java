package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.dto.CreditDto;
import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class CalculatorServiceImpl implements CalculatorService {
    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId) {

        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                offerService.createOffer(loanStatementRequestDto, false, false, logId),
                offerService.createOffer(loanStatementRequestDto, false, true, logId),
                offerService.createOffer(loanStatementRequestDto, true, true, logId),
                offerService.createOffer(loanStatementRequestDto, true, false, logId)
        ));
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));
        log.info("{} -- Формирование возможных предложений окончено. Передаем клиенту список в порядке уменьшения кредитной ставки.", logId);
        log.info("{} -- 1 вариант займа: {}", logId, offers.get(0));
        log.info("{} -- 2 вариант займа: {}", logId, offers.get(1));
        log.info("{} -- 3 вариант займа: {}", logId, offers.get(2));
        log.info("{} -- 4 вариант займа: {}", logId, offers.get(3));
        return offers;
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId) {
        log.info("{} -- Начинаем детальный расчет параметров кредита.", logId);
        CreditDto creditDto = creditService.calculateCredit(scoringDataDto, logId);
        log.info("{} -- Расчет окончен. Передаем клиенту полную информацию по кредиту. {}", logId, creditDto);
        return creditDto;
    }
}