package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.PaymentScheduleService;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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
        BigDecimal rate = scoringService.scoring(scoringDataDto);
        BigDecimal insurance = scoringDataDto.getIsInsuranceEnabled()
                ? insuranceService.calculateInsurance(amount, term)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate);

        List<PaymentScheduleElementDto> schedule = paymentScheduleService.createPaymentSchedule(amount, term, rate, monthlyPayment);

        BigDecimal psk = calculatePsk(amount, insurance, schedule);

        CreditDto creditDto = new CreditDto();          //todo может в builder?
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
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        BigDecimal rateMonth = rate.movePointLeft(2).divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_EVEN);
        BigDecimal tmp = rateMonth.add(BigDecimal.ONE).pow(term);        // tmp = (1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth).multiply(tmp)
                .divide(tmp.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_EVEN);

        return monthlyPayment;
    }

    //пск = (сумма платежей / сумма кредита - 1 ) / срок кредита в годах * 100
    public BigDecimal calculatePsk(BigDecimal amount, BigDecimal insurance, List<PaymentScheduleElementDto> schedule) {
        BigDecimal totalAmount = calculateTotalAmount(schedule).add(insurance);

        BigDecimal psk = totalAmount
                .divide(amount, 10, RoundingMode.HALF_EVEN)
                .subtract(BigDecimal.ONE)
                .movePointRight(2)
                .divide(BigDecimal.valueOf((schedule.size() - 1.0) / 12.0), 3, RoundingMode.HALF_EVEN);

        return psk;
    }

    public BigDecimal calculateTotalAmount(List<PaymentScheduleElementDto> schedule) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PaymentScheduleElementDto payment : schedule)
            totalAmount = totalAmount.add(payment.getTotalPayment());
        // System.out.println(totalAmount);
        return totalAmount;
    }
}
