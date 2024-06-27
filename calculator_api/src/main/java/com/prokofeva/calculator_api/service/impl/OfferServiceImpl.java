package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.OfferService;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final CreditService creditService;
    private final InsuranceService insuranceService;
    private final ScoringService scoringService;

    @Override
    public LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                                    boolean isInsuranceEnabled, boolean isSalaryClient, String logId) {
        log.info("{} -- Расчет предложения займа с параметрами: страховка кредита({}), зарплатный клиент({}).",
                logId, isInsuranceEnabled, isSalaryClient);
        BigDecimal amount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();
        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient,logId);
        BigDecimal insurance = isInsuranceEnabled
                ? insuranceService.calculateInsurance(amount, term,logId)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = creditService.calculateMonthlyPayment(amount, term, rate, logId);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).add(insurance);

        LoanOfferDto offerDto = LoanOfferDto.builder()
                .statementId(UUID.randomUUID())
                .requestedAmount(amount)
                .totalAmount(totalAmount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .build();

        log.info("{} -- Предложение сформировано.", logId);

        return offerDto;
    }
}
