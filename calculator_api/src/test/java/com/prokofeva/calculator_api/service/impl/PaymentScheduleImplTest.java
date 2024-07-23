package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.dto.PaymentScheduleElementDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentScheduleImplTest {

    @Autowired
    private PaymentScheduleImpl paymentSchedule;

    @Test
    void createPaymentSchedule() {
        BigDecimal amount = BigDecimal.valueOf(50000);
        Integer term = 6;
        BigDecimal rate = BigDecimal.valueOf(20.0);
        BigDecimal insurance = BigDecimal.valueOf(2000.0);
        BigDecimal monthlyPayment = BigDecimal.valueOf(8826.14);
        List<PaymentScheduleElementDto> schedule = paymentSchedule.createPaymentSchedule(amount, term, rate, monthlyPayment, insurance, "logId");

        assertNotNull(schedule);
        assertEquals(term + 1, schedule.size());

        PaymentScheduleElementDto payment0 = schedule.get(0);
        assertEquals(amount, payment0.getRemainingDebt());
        assertEquals(BigDecimal.ZERO, payment0.getInterestPayment());
        assertEquals(BigDecimal.ZERO, payment0.getDebtPayment());

        PaymentScheduleElementDto paymentLast = schedule.get(term);
        assertEquals(BigDecimal.ZERO, paymentLast.getRemainingDebt());
        assertEquals(payment0.getDate().plusMonths(term), paymentLast.getDate());

        PaymentScheduleElementDto payment1 = schedule.get(1);
        assertEquals(payment0.getDate().plusMonths(1), payment1.getDate());
        assertTrue(payment1.getInterestPayment().compareTo(paymentLast.getInterestPayment()) > 0);
        assertTrue(payment1.getDebtPayment().compareTo(paymentLast.getDebtPayment()) < 0);

    }
}