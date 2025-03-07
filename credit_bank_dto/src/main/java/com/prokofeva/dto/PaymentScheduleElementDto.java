package com.prokofeva.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Ежемесячный платеж по кредиту.")
@Data
@Builder
public class PaymentScheduleElementDto {

    @Schema(description = "Номер платежа")
    @NotNull
    private Integer number;

    @Schema(description = "Дата платежа")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    @Schema(description = "Общая сумма платежа")
    @NotNull
    private BigDecimal totalPayment;

    @Schema(description = "Выплата процентов")
    @NotNull
    private BigDecimal interestPayment;

    @Schema(description = "Выплата основного долга")
    @NotNull
    private BigDecimal debtPayment;

    @Schema(description = "Остаток")
    @NotNull
    private BigDecimal remainingDebt;
}