package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.model.dto.CreditDto;
import com.prokofeva.calculator_api.model.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.model.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.PaymentScheduleService;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final ScoringService scoringService;
    private final InsuranceService insuranceService;
    private final PaymentScheduleService paymentScheduleService;

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto, String logId) {
        BigDecimal amount = scoringDataDto.getAmount();
        Integer term = scoringDataDto.getTerm();

        log.info("{} -- Для принятия решения о выдаче займа и расчета ставки заявка должна пройти скоринг.", logId);

        BigDecimal rate = scoringService.scoring(scoringDataDto, logId);
        BigDecimal insurance = scoringDataDto.getIsInsuranceEnabled()
                ? insuranceService.calculateInsurance(amount, term, logId)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate, logId);

        List<PaymentScheduleElementDto> schedule = paymentScheduleService.createPaymentSchedule(amount, term, rate, monthlyPayment, insurance,logId);

        BigDecimal psk = calculatePsk(amount, rate, schedule, logId);

        return CreditDto.builder()
                .amount(amount)
                .term(term)
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .insuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .salaryClient(scoringDataDto.getIsSalaryClient())
                .paymentSchedule(schedule)
                .build();
    }

    // monthlyPayment = amount * rate / 12 * (1+rate/12)^term / ((1+rate/12)^term - 1)
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate, String logId) {
        log.info("{} -- Расчет суммы ежемесячного платежа по параметрам (сумма - {}, срок {} мес., ставка - {}%.", logId, amount, term, rate);
        BigDecimal rateMonth = rate.movePointLeft(2).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
        BigDecimal tmp = rateMonth.add(BigDecimal.ONE).pow(term);        // tmp = (1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth)
                .multiply(tmp)
                .divide(tmp.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);

        log.info("{} -- Ежемесячный платеж составляет {}.", logId, monthlyPayment);
        return monthlyPayment;
    }

    @Override
    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal rate, List<PaymentScheduleElementDto> schedule, String logId) {
        log.info("{} -- Расчет полной стоимости кредита.", logId);

        BigDecimal minRateI = BigDecimal.ZERO;
        BigDecimal maxRateI = rate;

        while (checkRateI(maxRateI, amount, schedule).signum() == 1) {
            minRateI = maxRateI;
            maxRateI = maxRateI.multiply(BigDecimal.valueOf(2));
        }

        BigDecimal validDif = BigDecimal.valueOf(100);
        while (minRateI.compareTo(maxRateI) < 0) {
            BigDecimal midRateI = maxRateI.add(minRateI).divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_EVEN);
            BigDecimal dif = checkRateI(midRateI, amount, schedule);
            if (dif.signum() == -1) {
                maxRateI = midRateI;
            } else {
                if (dif.compareTo(validDif) < 0)
                    break;

                minRateI = midRateI;
            }
        }

        BigDecimal psk = minRateI.multiply(BigDecimal.valueOf(365)).divide(BigDecimal.valueOf(30), 3, RoundingMode.HALF_EVEN);

        log.info("{} -- Полная стоимость кредита составляет {}.", logId, psk);

        return psk;
    }

    private BigDecimal checkRateI(BigDecimal rate, BigDecimal amount, List<PaymentScheduleElementDto> schedule) {
        BigDecimal dif = amount.negate().add(schedule.get(0).getTotalPayment());

        BigDecimal tmp = BigDecimal.ONE;
        BigDecimal tmpPow = rate.movePointLeft(2).add(BigDecimal.ONE);


        for (int i = 1; i < schedule.size(); i++) {
            tmp = tmp.multiply(tmpPow);
            BigDecimal payment = schedule.get(i).getTotalPayment().divide(tmp, 5, RoundingMode.HALF_EVEN);
            dif = dif.add(payment);
        }

        return dif;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PaymentScheduleElementDto payment : schedule)
            totalAmount = totalAmount.add(payment.getTotalPayment());

        return totalAmount;
    }
}
