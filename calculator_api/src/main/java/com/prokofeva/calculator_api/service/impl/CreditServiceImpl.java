package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
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
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        BigDecimal amount = scoringDataDto.getAmount();
        Integer term = scoringDataDto.getTerm();

        log.info("Для принятия решения о выдаче займа и расчета ставки заявка должна пройти скоринг.");

        BigDecimal rate = scoringService.scoring(scoringDataDto);
        BigDecimal insurance = scoringDataDto.getIsInsuranceEnabled()
                ? insuranceService.calculateInsurance(amount, term)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate);

        List<PaymentScheduleElementDto> schedule = paymentScheduleService.createPaymentSchedule(amount, term, rate,monthlyPayment);

        BigDecimal psk = calculatePsk(amount, insurance, schedule);

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(amount);
        creditDto.setTerm(term);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(schedule);

        return creditDto;
    }

    // monthlyPayment = amount * rate / 12 * (1+rate/12)^term / ((1+rate/12)^term - 1)
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        log.info("Расчет суммы ежемесячного платежа по параметрам (сумма - {}, срок {} мес., ставка - {}%.",amount,term,rate);
        BigDecimal rateMonth = rate.movePointLeft(2).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
        BigDecimal tmp = rateMonth.add(BigDecimal.ONE).pow(term);        // tmp = (1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth)
                .multiply(tmp)
                .divide(tmp.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);

        log.info("Ежемесячный платеж составляет {}.",monthlyPayment);
        return monthlyPayment;
    }

    // упрощеная формула: пск = (сумма платежей / сумма кредита - 1 ) / срок кредита в годах * 100
    @Override
    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal insurance, List<PaymentScheduleElementDto> schedule) {
        log.info("Расчет полной стоимости кредита.");

        BigDecimal totalAmount = calculateTotalAmount(schedule, insurance).add(insurance);

        BigDecimal psk = totalAmount
                .divide(amount, 10, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal.ONE)
                .movePointRight(2)
                .divide(BigDecimal.valueOf((schedule.size() - 1.0) / 12.0), 3, RoundingMode.HALF_EVEN);

        log.info("Полная стоимость кредита составляет {}.",psk);

        return psk;
    }

    @Override
    public BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule, BigDecimal insurance) {
        schedule.get(0).setTotalPayment(insurance);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PaymentScheduleElementDto payment : schedule)
            totalAmount = totalAmount.add(payment.getTotalPayment());

        return totalAmount;
    }
}
