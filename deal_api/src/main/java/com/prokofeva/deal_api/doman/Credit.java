package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.UUID;

public class Credit {
    private UUID creditId;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal monthlyPayment;
    private BigDecimal rate;
    private BigDecimal psk;
    private String paymentSchedule;     // todo jsonb
    private Boolean insuranceEnabled;
    private Boolean salaryClient;
    private CreditStatus creditStatus;
}
