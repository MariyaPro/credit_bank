package com.prokofeva.calculator_api.doman;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LoanOfferDto {

    private String statementId;

    private BigDecimal requestedAmount;

    private BigDecimal totalAmount;

    private Integer term;

    private BigDecimal monthlyPayment;

    private BigDecimal rate;

    private Boolean isInsuranceEnabled;

    private Boolean isSalaryClient;

}

