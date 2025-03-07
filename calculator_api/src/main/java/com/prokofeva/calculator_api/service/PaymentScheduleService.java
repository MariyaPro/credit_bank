package com.prokofeva.calculator_api.service;

import com.prokofeva.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentScheduleService {
    List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, Integer term,
                                                          BigDecimal totalRate,
                                                          BigDecimal monthlyPayment,
                                                          BigDecimal insurance, String logId);
}