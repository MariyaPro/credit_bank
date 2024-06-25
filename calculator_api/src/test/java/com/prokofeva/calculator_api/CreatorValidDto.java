package com.prokofeva.calculator_api;

import com.prokofeva.calculator_api.model.dto.*;
import com.prokofeva.calculator_api.model.enums.EmploymentPosition;
import com.prokofeva.calculator_api.model.enums.EmploymentStatus;
import com.prokofeva.calculator_api.model.enums.Gender;
import com.prokofeva.calculator_api.model.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CreatorValidDto {

    public static LoanStatementRequestDto createLoanStatementRequestDto() {
        return LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .email("mail.example@gmail.com")
                .birthdate(LocalDate.of(2000, 2, 21))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();
    }

    public static LoanOfferDto createLoanOfferDto() {
        return LoanOfferDto.builder()
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(111185))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9263.45))
                .rate(BigDecimal.valueOf(20.00))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    public static ScoringDataDto createScoringDataDto() {
        return ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(18)
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(2000, 2, 21))
                .passportSeries("1234")
                .passportNumber("123456")
                .maritalStatus(MaritalStatus.SINGLE)
                .dependentAmount(0)
                .employment(createEmploymentDto())
                .accountNumber("123456789")
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    public static EmploymentDto createEmploymentDto() {
        return EmploymentDto.builder()
                .status(EmploymentStatus.EMPLOYED)
                .employerInn("23366677711")
                .salary(BigDecimal.valueOf(70000))
                .position(EmploymentPosition.WORKER)
                .workExperienceTotal(36)
                .workExperienceCurrent(10)
                .build();
    }

    public static CreditDto createCreditDto() {
        return CreditDto.builder()
                .amount(BigDecimal.valueOf(50000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(8826.14))
                .rate(BigDecimal.valueOf(20.00))
                .psk(BigDecimal.valueOf(19.974))
                .insuranceEnabled(false)
                .salaryClient(false)
                .paymentSchedule(createPaymentSchedule())
                .build();
    }

    public static List<PaymentScheduleElementDto> createPaymentSchedule() {
        PaymentScheduleElementDto payment0 = PaymentScheduleElementDto.builder()
                .number(0)
                .date(LocalDate.now())
                .totalPayment(BigDecimal.ZERO)
                .interestPayment(BigDecimal.ZERO)
                .debtPayment(BigDecimal.ZERO)
                .remainingDebt(BigDecimal.valueOf(50000))
                .build();

        PaymentScheduleElementDto payment1 = PaymentScheduleElementDto.builder()
                .number(1)
                .date(LocalDate.now().plusMonths(1))
                .totalPayment(BigDecimal.valueOf(8826.14))
                .interestPayment(BigDecimal.valueOf(846.99))
                .debtPayment(BigDecimal.valueOf(7979.15))
                .remainingDebt(BigDecimal.valueOf(42020.85))
                .build();

        PaymentScheduleElementDto payment2 = PaymentScheduleElementDto.builder()
                .number(2)
                .date(LocalDate.now().plusMonths(2))
                .totalPayment(BigDecimal.valueOf(8826.14))
                .interestPayment(BigDecimal.valueOf(688.87))
                .debtPayment(BigDecimal.valueOf(8137.27))
                .remainingDebt(BigDecimal.valueOf(33883.58))
                .build();

        PaymentScheduleElementDto payment3 = PaymentScheduleElementDto.builder()
                .number(3)
                .date(LocalDate.now().plusMonths(3))
                .totalPayment(BigDecimal.valueOf(8826.14))
                .interestPayment(BigDecimal.valueOf(573.98))
                .debtPayment(BigDecimal.valueOf(8252.16))
                .remainingDebt(BigDecimal.valueOf(25631.42))
                .build();

        PaymentScheduleElementDto payment4 = PaymentScheduleElementDto.builder()
                .number(4)
                .date(LocalDate.now().plusMonths(4))
                .totalPayment(BigDecimal.valueOf(8826.14))
                .interestPayment(BigDecimal.valueOf(434.19))
                .debtPayment(BigDecimal.valueOf(8391.95))
                .remainingDebt(BigDecimal.valueOf(17239.47))
                .build();

        PaymentScheduleElementDto payment5 = PaymentScheduleElementDto.builder()
                .number(5)
                .date(LocalDate.now().plusMonths(5))
                .totalPayment(BigDecimal.valueOf(8826.14))
                .interestPayment(BigDecimal.valueOf(282.61))
                .debtPayment(BigDecimal.valueOf(8543.53))
                .remainingDebt(BigDecimal.valueOf(8695, 94))
                .build();

        PaymentScheduleElementDto payment6 = PaymentScheduleElementDto.builder()
                .number(6)
                .date(LocalDate.now().plusMonths(6))
                .totalPayment(BigDecimal.valueOf(8843.25))
                .interestPayment(BigDecimal.valueOf(147.31))
                .debtPayment(BigDecimal.valueOf(8695.94))
                .remainingDebt(BigDecimal.ZERO)
                .build();

        return List.of(payment0, payment1, payment2, payment3, payment4, payment5, payment6);
    }
}
