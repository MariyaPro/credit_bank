package com.prokofeva.calculator_api.service;

import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.PaymentScheduleElementDto;
import com.prokofeva.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {

    CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId);

    BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate, String logId);

    BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule);

    BigDecimal calculatePsk(BigDecimal amount, BigDecimal insurance, List<PaymentScheduleElementDto> schedule, String logId);
}
