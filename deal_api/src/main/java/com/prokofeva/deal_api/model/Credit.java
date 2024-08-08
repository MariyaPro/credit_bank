package com.prokofeva.deal_api.model;

import com.prokofeva.dto.PaymentScheduleElementDto;
import com.prokofeva.enums.CreditStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
public class Credit {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID creditId;

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
    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<PaymentScheduleElementDto> paymentSchedule;

    @NotNull
    private Boolean insuranceEnabled;

    @NotNull
    private Boolean salaryClient;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}