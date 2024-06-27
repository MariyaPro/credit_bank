package com.prokofeva.deal_api.dto;

import com.prokofeva.deal_api.enums.CreditStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Полный расчет параметров кредита.")
@Data
@Builder
public class CreditDto {
    
    private UUID creditId;
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

    @Schema(description = "График платежей")
    @NotNull
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Schema(description = "Влючена ли страховка кредита")
    @NotNull
    private Boolean insuranceEnabled;

    @Schema(description = "Зарплатный клиент")
    @NotNull
    private Boolean salaryClient;

    private CreditStatus creditStatus;
}