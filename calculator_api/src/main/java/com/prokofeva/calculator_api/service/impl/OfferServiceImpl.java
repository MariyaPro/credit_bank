package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.dto.LoanOfferDto;
import com.prokofeva.calculator_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.OfferService;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final CreditService creditService;
    private final InsuranceService insuranceService;
    private final ScoringService scoringService;

    @Override
    public LoanOfferDto createOffer(LoanStatementRequestDto loanStatementRequestDto,
                                    boolean isInsuranceEnabled, boolean isSalaryClient) {

        BigDecimal amount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();
        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal insurance = isInsuranceEnabled
                ? insuranceService.calculateInsurance(amount, term)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = creditService.calculateMonthlyPayment(amount, term, rate);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).add(insurance);

        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(amount);
        offerDto.setTotalAmount(totalAmount);
        offerDto.setTerm(term);
        offerDto.setMonthlyPayment(monthlyPayment);
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }
}
