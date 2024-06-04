package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {

    CreditDto calculateCredit(ScoringDataDto scoringDataDto);

    BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate);

    BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule);

    BigDecimal calculatePsk(BigDecimal amount, BigDecimal insurance, List<PaymentScheduleElementDto> schedule);
}
