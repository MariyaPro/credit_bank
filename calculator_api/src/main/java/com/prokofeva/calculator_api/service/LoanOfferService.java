package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanOfferService {

    @Value("${base_loan_rate}")
    private Double baseRate;

    public LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto,
                                        boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);

        BigDecimal totalAmount = calculateTotalAmount(loanStatementRequestDto.getAmount(),
                loanStatementRequestDto.getTerm(), rate, isInsuranceEnabled);

        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(loanStatementRequestDto.getAmount());
        offerDto.setTotalAmount(totalAmount);
        offerDto.setTerm(loanStatementRequestDto.getTerm());
        offerDto.setMonthlyPayment(totalAmount.multiply(BigDecimal.valueOf(1.0 / loanStatementRequestDto.getTerm())));
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }

    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        Double rate = baseRate;
        if (isInsuranceEnabled) rate -= 3.0;
        if (isSalaryClient) rate -= 1.0;
        return BigDecimal.valueOf(rate);
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount, Integer term, BigDecimal rate, boolean isInsuranceEnabled) {
        BigDecimal totalAmount = isInsuranceEnabled ? amount.add(calculateInsurance(amount, term)) : amount;
        totalAmount = totalAmount.add(totalAmount
                .multiply(rate)
                .multiply(BigDecimal.valueOf(term / 12.0)));
        return totalAmount;
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        double rate = 3.0;
        if (term < 12) rate += 1.0;
        if (term >= 60) rate -= 1.0;

        if (amount.compareTo(BigDecimal.valueOf(100000.0)) <= 0) rate += 1.0;
        if (amount.compareTo(BigDecimal.valueOf(1000000.0)) >= 0) rate -= 1.0;

        BigDecimal amountInsurance = amount.multiply(BigDecimal.valueOf(rate)).multiply(BigDecimal.valueOf(term / 12.0));

        BigDecimal maxInsurance = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal minInsurance = amount.multiply(BigDecimal.valueOf(0.05));

        return amountInsurance.max(minInsurance).min(maxInsurance);
    }

}
