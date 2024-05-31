package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.LoanOfferDto;
import com.prokofeva.calculator_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
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
    @Value("${prescoring.min_age}")
    private Integer prescoringMinAge;

    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Перед расчетом возможных вариантов заявка должна пройти прескоринг.");
        if (!prescoring(loanStatementRequestDto.getBirthdate())) {
            log.info("Заявка не прошла прескоринг.");
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }
        log.info("Заявка удачно прошла прескоринг. Идет формирование предложений.");
        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                offerService.createOffer(loanStatementRequestDto, false, false),
                offerService.createOffer(loanStatementRequestDto, false, true),
                offerService.createOffer(loanStatementRequestDto, true, true),
                offerService.createOffer(loanStatementRequestDto, true, false)
        ));
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));
        log.info("Формирование возможных предложений окончено. Передаем клиенту список в порядке уменьшения кредитной ставки.");
        log.info("1 вариант займа: {}", offers.get(0));
        log.info("2 вариант займа: {}", offers.get(1));
        log.info("3 вариант займа: {}", offers.get(2));
        log.info("4 вариант займа: {}", offers.get(3));
        return offers;
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        log.info("Начинаем детальный расчет параметров кредита.");
        CreditDto creditDto = creditService.calculateCredit(scoringDataDto);
        log.info("Расчет окончен. Передаем клиенту полную информацию по кредиту. " + creditDto.toString());
        return creditDto;
    }

    private boolean prescoring(LocalDate birthdate) {
        log.info("...прескоринг...");
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
    }
}