package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.service.InsuranceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InsuranceServiceImpl implements InsuranceService {
    @Value("${rate_insurance.base}")
    private BigDecimal rateInsuranceBase;

    @Value("${rate_insurance.term.short}")
    private Integer rateInsuranceTermShort;

    @Value("${rate_insurance.term.long}")
    private Integer rateInsuranceTermLong;

    @Value("${rate_insurance.amount_loan.small}")
    private BigDecimal rateInsuranceAmountSmall;

    @Value("${rate_insurance.amount_loan.big}")
    private BigDecimal rateInsuranceAmountBig;

    @Value("${rate_insurance.correction.short_term}")
    private BigDecimal rateInsuranceCorrectionShortTerm;

    @Value("${rate_insurance.correction.long_term}")
    private BigDecimal rateInsuranceCorrectionLongTerm;

    @Value("${rate_insurance.correction.small_amount}")
    private BigDecimal rateInsuranceCorrectionSmallAmount;

    @Value("${rate_insurance.correction.big_amount}")
    private BigDecimal rateInsuranceCorrectionBigAmount;

    @Override
    public BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        BigDecimal rate = rateInsuranceBase;
        if (term < rateInsuranceTermShort) {
            rate = rate.add(rateInsuranceCorrectionShortTerm);
        }
        if (term >= rateInsuranceTermLong) {
            rate = rate.add(rateInsuranceCorrectionLongTerm);
        }

        if (amount.compareTo(rateInsuranceAmountSmall) <= 0) {
            rate = rate.add(rateInsuranceCorrectionSmallAmount);
        }
        if (amount.compareTo(rateInsuranceAmountBig) >= 0) {
            rate = rate.add(rateInsuranceCorrectionBigAmount);
        }

        return amount.multiply(rate).movePointLeft(2).setScale(2, RoundingMode.HALF_EVEN);
    }
}
