package com.prokofeva.calculator_api.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class InsuranceServiceImplTest {
    @Autowired
    private InsuranceServiceImpl insuranceService;

    @Test
    void calculateInsurance() {
        BigDecimal insuranceNormal = insuranceService.calculateInsurance(
                insuranceService.getRateInsuranceAmountBig().subtract(BigDecimal.ONE)
                , insuranceService.getRateInsuranceTermLong() - 1);
        BigDecimal insurance_BigAmount = insuranceService.calculateInsurance(
                insuranceService.getRateInsuranceAmountBig().add(BigDecimal.ONE)
                , insuranceService.getRateInsuranceTermLong() - 1);
        BigDecimal insurance_SmallAmount = insuranceService.calculateInsurance(
                insuranceService.getRateInsuranceAmountSmall().subtract(BigDecimal.ONE)
                , insuranceService.getRateInsuranceTermLong() - 1);
        BigDecimal insurance_LongTerm = insuranceService.calculateInsurance(
                insuranceService.getRateInsuranceAmountBig().subtract(BigDecimal.ONE)
                , insuranceService.getRateInsuranceTermLong() + 1);
        BigDecimal insurance_ShortTerm = insuranceService.calculateInsurance(
                insuranceService.getRateInsuranceAmountBig().subtract(BigDecimal.ONE)
                , insuranceService.getRateInsuranceTermShort() - 1);

        assertNotEquals(insuranceNormal, insurance_BigAmount);
        assertNotEquals(insuranceNormal, insurance_SmallAmount);
        assertNotEquals(insuranceNormal, insurance_LongTerm);
        assertNotEquals(insuranceNormal, insurance_ShortTerm);

        assertNotEquals(insurance_BigAmount, insurance_SmallAmount);
        assertNotEquals(insurance_LongTerm, insurance_ShortTerm);
    }
}