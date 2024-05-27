package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

public interface CreditService {

    CreditDto calculateCredit(ScoringDataDto scoringDataDto);

    BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate);

    BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule);
}
