package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.PaymentScheduleElementDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.PaymentScheduleService;
import com.prokofeva.calculator_api.service.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.yaml")
class CreditServiceImplTest {

    @Value("${valid_difference_psk}")
    private BigDecimal validDifPsk;

    @Value("${valid_difference_monthlyPayment}")
    private BigDecimal validDifMonthlyPayment;

    @Mock
    private ScoringService scoringService;

    @Mock
    private PaymentScheduleService paymentScheduleService;

    @InjectMocks
    private CreditServiceImpl creditService;

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = CreatorValidDto.createScoringDataDto();
        when(scoringService.scoring(any())).thenReturn(BigDecimal.valueOf(20.00));
        when(paymentScheduleService.createPaymentSchedule(any(), any(), any(), any(), any())).thenReturn(CreatorValidDto.createPaymentSchedule());


        CreditDto creditDto = creditService.calculateCredit(scoringDataDto);

        assertNotNull(creditDto);

        assertEquals(creditDto.getAmount(), scoringDataDto.getAmount());
        assertEquals(creditDto.getTerm(), scoringDataDto.getTerm());
        assertEquals(creditDto.getInsuranceEnabled(), scoringDataDto.getIsInsuranceEnabled());
        assertEquals(creditDto.getSalaryClient(), scoringDataDto.getIsSalaryClient());

        assertNotNull(creditDto.getRate());
        assertNotNull(creditDto.getMonthlyPayment());
        assertNotNull(creditDto.getPsk());
        assertNotNull(creditDto.getPaymentSchedule());
    }

    @Test
    void calculateMonthlyPayment() {
        BigDecimal payment1 = creditService.calculateMonthlyPayment(
                BigDecimal.valueOf(50000), 6, BigDecimal.valueOf(30));
        BigDecimal payment2 = creditService.calculateMonthlyPayment(
                BigDecimal.valueOf(300000), 18, BigDecimal.valueOf(25));
        BigDecimal payment3 = creditService.calculateMonthlyPayment(
                BigDecimal.valueOf(5000000), 120, BigDecimal.valueOf(15));

        BigDecimal payment1Ex = BigDecimal.valueOf(9077.5);
        BigDecimal payment2Ex = BigDecimal.valueOf(20157.54);
        BigDecimal payment3Ex = BigDecimal.valueOf(80667.48);

        BigDecimal dif1 = payment1.subtract(payment1Ex).abs().movePointLeft(2).divide(payment1Ex, 3, RoundingMode.HALF_EVEN);
        BigDecimal dif2 = payment2.subtract(payment2Ex).abs().movePointLeft(2).divide(payment2Ex, 3, RoundingMode.HALF_EVEN);
        BigDecimal dif3 = payment3.subtract(payment3Ex).abs().movePointLeft(2).divide(payment3Ex, 3, RoundingMode.HALF_EVEN);

        assertTrue(validDifMonthlyPayment.compareTo(dif1) >= 0);
        assertTrue(validDifMonthlyPayment.compareTo(dif2) >= 0);
        assertTrue(validDifMonthlyPayment.compareTo(dif3) >= 0);
    }

    @Test
    void calculatePsk() {
        //без страховки
        List<PaymentScheduleElementDto> schedule = CreatorValidDto.createPaymentSchedule();

        BigDecimal psk1 = creditService.calculatePsk(
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(20),
                schedule);
        BigDecimal psk1Ex = BigDecimal.valueOf(19.937);
        BigDecimal dif1 = psk1.subtract(psk1Ex).abs().movePointRight(2).divide(psk1Ex, 3, RoundingMode.HALF_EVEN);

        assertTrue(validDifPsk.compareTo(dif1) >= 0);

        //со страховкой
        schedule.get(0).setTotalPayment(BigDecimal.valueOf(2000));
        BigDecimal psk2 = creditService.calculatePsk(
                BigDecimal.valueOf(50000),
                BigDecimal.valueOf(20),
                schedule);
        BigDecimal psk2Ex = BigDecimal.valueOf(34.393);
        BigDecimal dif2 = psk2.subtract(psk2Ex).abs().movePointRight(2).divide(psk1Ex, 3, RoundingMode.HALF_EVEN);

        assertTrue(validDifPsk.compareTo(dif2) >= 0);
    }

    @Test
    void calculateTotalAmount() {
        List<PaymentScheduleElementDto> schedule = CreatorValidDto.createPaymentSchedule();
        BigDecimal totalAmount = creditService.calculateTotalAmount(schedule);

        assertEquals(totalAmount, BigDecimal.valueOf(52973.95));

        for (PaymentScheduleElementDto payment : schedule)
            payment.setTotalPayment(BigDecimal.valueOf(1000));

        totalAmount = creditService.calculateTotalAmount(schedule);
        assertEquals(totalAmount, BigDecimal.valueOf(1000L * schedule.size()));
    }
}