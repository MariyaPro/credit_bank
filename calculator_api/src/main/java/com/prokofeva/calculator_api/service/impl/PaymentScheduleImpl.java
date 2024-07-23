package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.service.PaymentScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentScheduleImpl implements PaymentScheduleService {

    @Override
    public List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, Integer term,
                                                                 BigDecimal rate,
                                                                 BigDecimal monthlyPayment,
                                                                 BigDecimal insurance,
                                                                 String logId) {
        log.info("{} -- Идет расчет графика платежей.", logId);

        List<PaymentScheduleElementDto> schedule = new ArrayList<>();
        BigDecimal remainingDebt = amount;
        BigDecimal rateDay = rate.movePointLeft(2).divide(BigDecimal.valueOf(365), 10, RoundingMode.HALF_EVEN);
        LocalDate date = LocalDate.now();

        addFirstPayment(schedule, amount, insurance);

        log.info("{} -- Платеж №0: {}.", logId, schedule.get(0));

        for (int i = 1; i <= term; i++) {
            LocalDate datePayment = date.plusMonths(1);
            long days = ChronoUnit.DAYS.between(date, datePayment);
            BigDecimal interestPayment = remainingDebt.multiply(rateDay).multiply(BigDecimal.valueOf(days)).setScale(2, RoundingMode.HALF_EVEN);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);
            date = datePayment;

            PaymentScheduleElementDto scheduleElement = PaymentScheduleElementDto.builder()
                    .number(i)
                    .date(datePayment)
                    .totalPayment(monthlyPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build();

            log.info("{} -- Платеж №{}: {}.", logId, i, scheduleElement);

            schedule.add(scheduleElement);
        }

        fixLastPayment(schedule.get(term), logId);

        log.info("{} -- Платеж №{}: {}.", logId, term, schedule.get(term));
        log.info("{} -- Формирование графика платежей завершено.", logId);

        return schedule;
    }

    private void addFirstPayment(List<PaymentScheduleElementDto> schedule, BigDecimal amount, BigDecimal insurance) {
        PaymentScheduleElementDto payment = PaymentScheduleElementDto.builder()
                .number(0)
                .date(LocalDate.now())
                .totalPayment(insurance)
                .interestPayment(BigDecimal.ZERO)
                .debtPayment(BigDecimal.ZERO)
                .remainingDebt(amount)
                .build();
        schedule.add(payment);
    }

    private void fixLastPayment(PaymentScheduleElementDto payment, String logId) {
        log.info("{} -- Коррекция последнего платежа.", logId);
        BigDecimal totalPayment = payment.getTotalPayment().add(payment.getRemainingDebt());
        payment.setTotalPayment(totalPayment);
        payment.setRemainingDebt(BigDecimal.ZERO);
    }
}
