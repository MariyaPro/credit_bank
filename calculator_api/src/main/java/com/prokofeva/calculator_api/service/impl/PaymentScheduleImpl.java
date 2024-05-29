package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentScheduleImpl implements PaymentScheduleService {

    @Override
    public List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, Integer term,
                                                                 BigDecimal rate, BigDecimal monthlyPayment
    ) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        BigDecimal rateDay = rate.movePointLeft(2).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_EVEN);
        LocalDate date = LocalDate.now();

        addFirstPayment(schedule, amount);

        for (int i = 1; i <= term; i++) {
            LocalDate datePayment = date.plusMonths(1);
            long days = ChronoUnit.DAYS.between(date, datePayment);
            BigDecimal interestPayment = remainingDebt.multiply(rateDay).multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);
            date = datePayment;

            PaymentScheduleElementDto scheduleElement = new PaymentScheduleElementDto();        //todo может в builder?
            scheduleElement.setNumber(i);
            scheduleElement.setDate(datePayment);
            scheduleElement.setTotalPayment(monthlyPayment);
            scheduleElement.setInterestPayment(interestPayment);
            scheduleElement.setDebtPayment(debtPayment);
            scheduleElement.setRemainingDebt(remainingDebt);

            schedule.add(scheduleElement);
        }
        fixLastPayment(schedule.get(term));

        return schedule;
    }

    private void addFirstPayment(List<PaymentScheduleElementDto> schedule, BigDecimal amount) {
        PaymentScheduleElementDto payment = new PaymentScheduleElementDto();
        payment.setNumber(0);
        payment.setDate(LocalDate.now());
        payment.setTotalPayment(BigDecimal.ZERO);
        payment.setInterestPayment(BigDecimal.ZERO);
        payment.setDebtPayment(BigDecimal.ZERO);
        payment.setRemainingDebt(amount);
        schedule.add(payment);
    }

    private void fixLastPayment(PaymentScheduleElementDto payment) {
        BigDecimal totalPayment = payment.getTotalPayment().add(payment.getRemainingDebt());
        payment.setTotalPayment(totalPayment);
        payment.setRemainingDebt(BigDecimal.ZERO);
    }
}
