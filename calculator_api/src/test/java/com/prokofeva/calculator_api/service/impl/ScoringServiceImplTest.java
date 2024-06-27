package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.dto.ScoringDataDto;
import com.prokofeva.calculator_api.enums.EmploymentPosition;
import com.prokofeva.calculator_api.enums.EmploymentStatus;
import com.prokofeva.calculator_api.enums.MaritalStatus;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ScoringServiceImplTest {
    @Autowired
    private ScoringServiceImpl scoringService;

    @Test
    void calculateRate() {

        BigDecimal rate1 = scoringService.calculateRate(true, true, "logId");
        BigDecimal rate2 = scoringService.calculateRate(false, false, "logId");
        BigDecimal rate3 = scoringService.calculateRate(false, true, "logId");
        BigDecimal rate4 = scoringService.calculateRate(true, false, "logId");

        assertEquals(rate1, BigDecimal.valueOf(16.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(rate2, BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(rate3, BigDecimal.valueOf(19.00).setScale(2, RoundingMode.HALF_EVEN));
        assertEquals(rate4, BigDecimal.valueOf(17.00).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void scoringValid() {
        ScoringDataDto scoringDataDto = CreatorValidDto.createScoringDataDto();

        BigDecimal rate = scoringService.scoring(scoringDataDto, "logId");

        assertEquals(rate, BigDecimal.valueOf(20.00).setScale(2, RoundingMode.HALF_EVEN));

        scoringDataDto.setIsSalaryClient(true);
        scoringDataDto.setIsInsuranceEnabled(true);
        scoringDataDto.getEmployment().setPosition(EmploymentPosition.MID_MANAGER);
        scoringDataDto.setMaritalStatus(MaritalStatus.MARRIED);

        rate = scoringService.scoring(scoringDataDto, "logId");

        assertEquals(rate, BigDecimal.valueOf(12.00).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void scoringDeniedLoanException() {
        // статус - безработный
        ScoringDataDto scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.getEmployment().setStatus(EmploymentStatus.UNEMPLOYED);
        BigDecimal rate = null;
        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: employment status does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }

        // запрашиваемая сумма больше 25 зарплат
        scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.setAmount(BigDecimal.valueOf(5000000));
        scoringDataDto.getEmployment().setSalary(BigDecimal.valueOf(10000));

        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: the possible loan amount has been exceeded.".getBytes(StandardCharsets.UTF_8));
        }

        // ограничение по возрасту
        scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.setBirthdate(LocalDate.now().minusYears(19));
        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: age does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }

        scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.setBirthdate(LocalDate.now().minusYears(66));
        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: age does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }

        //общий и текущий стаж работы
        scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.getEmployment().setWorkExperienceTotal(17);
        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: work experience does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }

        scoringDataDto = CreatorValidDto.createScoringDataDto();
        scoringDataDto.getEmployment().setWorkExperienceCurrent(2);
        try {
            rate = scoringService.scoring(scoringDataDto, "logId");
        } catch (Exception e) {
            assertNull(rate);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: work experience does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }
        assertNull(rate);
    }

}