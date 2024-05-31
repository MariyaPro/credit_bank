package com.prokofeva.calculator_api.doman.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentScheduleElementDto {

    @NotNull
    private Integer number;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @NotNull
    private BigDecimal totalPayment;

    @NotNull
    private BigDecimal interestPayment;

    @NotNull
    private BigDecimal debtPayment;

    @NotNull
    private BigDecimal remainingDebt;
}

