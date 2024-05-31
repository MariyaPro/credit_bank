package com.prokofeva.calculator_api.doman.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Полный расчет параметров кредита.")
@Data
public class CreditDto {

    @Schema(description = "Сумма кредита")
    @NotNull
    private BigDecimal amount;

    @Schema(description = "Срок кредита")
    @NotNull
    private Integer term;

    @Schema(description = "Ежемячный платеж")
    @NotNull
    private BigDecimal monthlyPayment;

    @Schema(description = "Годовая процентная ставка")
    @NotNull
    private BigDecimal rate;

    @Schema(description = "Полная стоимость кредита")
    @NotNull
    private BigDecimal psk;

    @Schema(description = "Влючена ли страховка кредита")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент")
    @NotNull
    private Boolean isSalaryClient;

    @Schema(description = "График платежей")
    @NotNull
    private List<PaymentScheduleElementDto> paymentSchedule;
}

