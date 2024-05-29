package com.prokofeva.calculator_api.doman.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreditDto {

    @NotNull
    private BigDecimal amount;
    @NotNull
    private Integer term;
    @NotNull
    private BigDecimal monthlyPayment;
    @NotNull
    private BigDecimal rate;
    @NotNull
    private BigDecimal psk;
    @NotNull
    private Boolean isInsuranceEnabled;
    @NotNull
    private Boolean isSalaryClient;
    @NotNull
    private List<PaymentScheduleElementDto> paymentSchedule;
}

