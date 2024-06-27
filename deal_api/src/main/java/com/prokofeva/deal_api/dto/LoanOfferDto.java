package com.prokofeva.deal_api.dto;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Предложение условий займа")
@Data
@Builder
@TypeDef(name = "json", typeClass = JsonType.class)
public class LoanOfferDto {

    @Schema(description = "Id предложения")
    @NotNull
    private UUID statementId;

    @Schema(description = "Запрашиваемая сумма")
    @NotNull
    private BigDecimal requestedAmount;

    @Schema(description = "Общая сумма выплат")
    @NotNull
    private BigDecimal totalAmount;

    @Schema(description = "Срок кредита")
    @NotNull
    private Integer term;

    @Schema(description = "Ежемесячный платеж")
    @NotNull
    private BigDecimal monthlyPayment;

    @Schema(description = "Годовая процентная ставка")
    @NotNull
    private BigDecimal rate;

    @Schema(description = "Включена ли страховка кредита")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент")
    @NotNull
    private Boolean isSalaryClient;
}