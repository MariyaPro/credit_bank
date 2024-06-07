package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.PaymentScheduleElementDto;
import com.prokofeva.deal_api.doman.enums.CreditStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Credit {
    @Id
    @GeneratedValue
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
    @Type(type = "jsonb")
    private List<PaymentScheduleElementDto> paymentSchedule;

    @NotNull
    private Boolean insuranceEnabled;

    @NotNull
    private Boolean salaryClient;

    @NotNull
    private CreditStatus creditStatus;

}
