package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.model.dto.CreditDto;
import com.prokofeva.calculator_api.model.dto.LoanOfferDto;
import com.prokofeva.calculator_api.model.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.model.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class CalculatorServiceImpl implements CalculatorService {
    @Value("${prescoring_min_age}")
    private Integer prescoringMinAge;

    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId) {

        log.info("{} -- Перед расчетом возможных вариантов заявка должна пройти прескоринг.", logId);
        if (!prescoring(loanStatementRequestDto.getBirthdate(), logId)) {
            log.error("{} -- Заявка не прошла прескоринг.", logId);
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }
        log.info("{} -- Заявка удачно прошла прескоринг. Идет формирование предложений.", logId);
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

    private boolean prescoring(LocalDate birthdate, String logId) {
        log.info("{} -- прескоринг...", logId);
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
    }
}