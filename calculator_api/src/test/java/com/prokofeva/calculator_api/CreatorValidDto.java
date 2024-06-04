package com.prokofeva.calculator_api;

import com.prokofeva.calculator_api.doman.enums.EmploymentStatusEnum;
import com.prokofeva.calculator_api.doman.dto.*;
import com.prokofeva.calculator_api.doman.enums.GenderEnum;
import com.prokofeva.calculator_api.doman.enums.MaritalStatusEnum;
import com.prokofeva.calculator_api.doman.enums.PositionEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreatorValidDto {

    public static LoanStatementRequestDto createLoanStatementRequestDto() {
        LoanStatementRequestDto loanRequestDto = new LoanStatementRequestDto();

        loanRequestDto.setAmount(BigDecimal.valueOf(100000));
        loanRequestDto.setTerm(12);
        loanRequestDto.setFirstName("FirstName");
        loanRequestDto.setLastName("LastName");
        loanRequestDto.setMiddleName("MiddleName");
        loanRequestDto.setEmail("mail.example@gmail.com");
        loanRequestDto.setBirthdate(LocalDate.of(2000, 2, 21));
        loanRequestDto.setPassportSeries("1234");
        loanRequestDto.setPassportNumber("123456");

        return loanRequestDto;
    }

    public static LoanOfferDto createLoanOfferDto(){
        LoanOfferDto loanOfferDto = new LoanOfferDto();

        loanOfferDto.setStatementId(UUID.randomUUID());
        loanOfferDto.setRequestedAmount(BigDecimal.valueOf(100000));
        loanOfferDto.setTotalAmount(BigDecimal.valueOf(111185));
        loanOfferDto.setTerm(12);
        loanOfferDto.setMonthlyPayment(BigDecimal.valueOf(9263.45));
        loanOfferDto.setRate(BigDecimal.valueOf(20.00));
        loanOfferDto.setIsInsuranceEnabled(false);
        loanOfferDto.setIsSalaryClient(false);

        return loanOfferDto;
    }


    public static ScoringDataDto createScoringDataDto() {
        ScoringDataDto scoringDataDto = new ScoringDataDto();

        scoringDataDto.setAmount(BigDecimal.valueOf(100000));
        scoringDataDto.setTerm(18);
        scoringDataDto.setFirstName("FirstName");
        scoringDataDto.setLastName("LastName");
        scoringDataDto.setMiddleName("MiddleName");
        scoringDataDto.setGender(GenderEnum.MALE);
        scoringDataDto.setBirthdate(LocalDate.of(2000, 2, 21));
        scoringDataDto.setPassportSeries("1234");
        scoringDataDto.setPassportNumber("123456");
        scoringDataDto.setMaritalStatus(MaritalStatusEnum.SINGLE);
        scoringDataDto.setDependentAmount(0);
        scoringDataDto.setEmployment(createEmploymentDto());
        scoringDataDto.setAccountNumber("123456789");
        scoringDataDto.setIsInsuranceEnabled(false);
        scoringDataDto.setIsSalaryClient(false);

        return scoringDataDto;
    }

    public static EmploymentDto createEmploymentDto() {
        EmploymentDto employmentDto = new EmploymentDto();

        employmentDto.setEmploymentStatus(EmploymentStatusEnum.BUSY);
        employmentDto.setEmployerINN("23366677711");
        employmentDto.setSalary(BigDecimal.valueOf(70000));
        employmentDto.setPosition(PositionEnum.EMPLOYEE);
        employmentDto.setWorkExperienceTotal(36);
        employmentDto.setWorkExperienceCurrent(10);

        return employmentDto;
    }

    public static CreditDto createCreditDto(){
        CreditDto creditDto = new CreditDto();

        creditDto.setAmount(BigDecimal.valueOf(50000));
        creditDto.setTerm(6);
        creditDto.setMonthlyPayment(BigDecimal.valueOf(8826.14));
        creditDto.setRate(BigDecimal.valueOf(20.00));
        creditDto.setPsk(BigDecimal.valueOf(19.974));
        creditDto.setIsInsuranceEnabled(false);
        creditDto.setIsSalaryClient(false);
        creditDto.setPaymentSchedule(createPaymentSchedule());

        return creditDto;
    }

    public static List<PaymentScheduleElementDto> createPaymentSchedule() {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        PaymentScheduleElementDto payment0 = new PaymentScheduleElementDto();
        payment0.setNumber(0);
        payment0.setDate(LocalDate.now());
        payment0.setTotalPayment(BigDecimal.ZERO);
        payment0.setInterestPayment(BigDecimal.ZERO);
        payment0.setDebtPayment(BigDecimal.ZERO);
        payment0.setRemainingDebt(BigDecimal.valueOf(50000));

        PaymentScheduleElementDto payment1 = new PaymentScheduleElementDto();
        payment1.setNumber(1);
        payment1.setDate(LocalDate.now().plusMonths(1));
        payment1.setTotalPayment(BigDecimal.valueOf(8826.14));
        payment1.setInterestPayment(BigDecimal.valueOf(846.99));
        payment1.setDebtPayment(BigDecimal.valueOf(7979.15));
        payment1.setRemainingDebt(BigDecimal.valueOf(42020.85));

        PaymentScheduleElementDto payment2 = new PaymentScheduleElementDto();
        payment2.setNumber(2);
        payment2.setDate(LocalDate.now().plusMonths(2));
        payment2.setTotalPayment(BigDecimal.valueOf(8826.14));
        payment2.setInterestPayment(BigDecimal.valueOf(688.87));
        payment2.setDebtPayment(BigDecimal.valueOf(8137.27));
        payment2.setRemainingDebt(BigDecimal.valueOf(33883.58));

        PaymentScheduleElementDto payment3 = new PaymentScheduleElementDto();
        payment3.setNumber(3);
        payment3.setDate(LocalDate.now().plusMonths(3));
        payment3.setTotalPayment(BigDecimal.valueOf(8826.14));
        payment3.setInterestPayment(BigDecimal.valueOf(573.98));
        payment3.setDebtPayment(BigDecimal.valueOf(8252.16));
        payment3.setRemainingDebt(BigDecimal.valueOf(25631.42));

        PaymentScheduleElementDto payment4 = new PaymentScheduleElementDto();
        payment4.setNumber(4);
        payment4.setDate(LocalDate.now().plusMonths(4));
        payment4.setTotalPayment(BigDecimal.valueOf(8826.14));
        payment4.setInterestPayment(BigDecimal.valueOf(434.19));
        payment4.setDebtPayment(BigDecimal.valueOf(8391.95));
        payment4.setRemainingDebt(BigDecimal.valueOf(17239.47));

        PaymentScheduleElementDto payment5 = new PaymentScheduleElementDto();
        payment5.setNumber(5);
        payment5.setDate(LocalDate.now().plusMonths(5));
        payment5.setTotalPayment(BigDecimal.valueOf(8826.14));
        payment5.setInterestPayment(BigDecimal.valueOf(282.61));
        payment5.setDebtPayment(BigDecimal.valueOf(8543.53));
        payment5.setRemainingDebt(BigDecimal.valueOf(8695,94));

        PaymentScheduleElementDto payment6 = new PaymentScheduleElementDto();
        payment6.setNumber(6);
        payment6.setDate(LocalDate.now().plusMonths(6));
        payment6.setTotalPayment(BigDecimal.valueOf(8843.25));
        payment6.setInterestPayment(BigDecimal.valueOf(147.31));
        payment6.setDebtPayment(BigDecimal.valueOf(8695.94));
        payment6.setRemainingDebt(BigDecimal.ZERO);

        schedule.add(payment0);
        schedule.add(payment1);
        schedule.add(payment2);
        schedule.add(payment3);
        schedule.add(payment4);
        schedule.add(payment5);
        schedule.add(payment6);

        return schedule;
    }


}
