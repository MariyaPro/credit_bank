package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.*;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class CalculatorService {

    @Value("${base_loan_rate}")
    private Double baseLoanRate;

    private final MathContext mathContextDoc = new MathContext(2);
    private final MathContext mathContextCalc = new MathContext(15);

    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        if (!prescoring(loanStatementRequestDto.getBirthdate())) {
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }

        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                createLoanOffer(loanStatementRequestDto, false, false),
                createLoanOffer(loanStatementRequestDto, false, true),
                createLoanOffer(loanStatementRequestDto, true, true),
                createLoanOffer(loanStatementRequestDto, true, false)
        ));
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate())); //todo ???????????

        return offers;
    }

    private boolean prescoring(LocalDate birthdate) {       //todo ????????
        return LocalDate.now().minusYears(18).isAfter(birthdate);
    }

    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto,
                                         boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal amount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();

        BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);

        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, rate);

        BigDecimal insurance = isInsuranceEnabled ? calculateInsurance(amount, term) : BigDecimal.ZERO;

        BigDecimal psk = calculatePsk(term, monthlyPayment, insurance);

        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(amount);
        offerDto.setTotalAmount(psk);
        offerDto.setTerm(term);
        offerDto.setMonthlyPayment(monthlyPayment);
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }

    private BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        Double rate = baseLoanRate;
        if (isInsuranceEnabled) {
            rate -= 3.0;
        }
        if (isSalaryClient) {
            rate -= 1.0;
        }
        return BigDecimal.valueOf(rate);
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        double rate = 3.0;
        if (term < 12) rate += 1.0;
        if (term >= 60) rate -= 1.0;

        if (amount.compareTo(BigDecimal.valueOf(100000.0)) <= 0) rate += 1.0;
        if (amount.compareTo(BigDecimal.valueOf(1000000.0)) >= 0) rate -= 1.0;

        BigDecimal amountInsurance = amount.multiply(BigDecimal.valueOf(rate * 0.01)).multiply(BigDecimal.valueOf(term / 12.0));

        BigDecimal maxInsurance = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal minInsurance = amount.multiply(BigDecimal.valueOf(0.05));

        return amountInsurance.max(minInsurance).min(maxInsurance).round(mathContextDoc);
    }

    // monthlyPayment = amount * rate / 12 * (1+rate/12)^term / ((1+rate/12)^term - 1)
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        BigDecimal rateMonth = rate.divide(BigDecimal.valueOf(12), mathContextCalc);
        BigDecimal helper = rateMonth.add(BigDecimal.ONE).pow(term);        //todo: name? helper = (1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth).multiply(helper).divide(helper.add(BigDecimal.valueOf(-1)), mathContextDoc);

        return monthlyPayment;
    }

    private BigDecimal calculatePsk(Integer term, BigDecimal monthlyPayment, BigDecimal insurance) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term)).add(insurance);
    }

    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        BigDecimal amount = scoringDataDto.getAmount();
        Integer term = scoringDataDto.getTerm();
        BigDecimal insurance = scoringDataDto.getIsInsuranceEnabled() ? calculateInsurance(amount, term) : BigDecimal.ZERO;

        BigDecimal totalRate = scoring(scoringDataDto);

        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, term, totalRate);

        BigDecimal psk = calculatePsk(term, monthlyPayment, insurance);

        List<PaymentScheduleElementDto> schedule = createPaymentSchedule(amount, term, totalRate, monthlyPayment);

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(amount);
        creditDto.setTerm(term);
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(totalRate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(schedule);

        return creditDto;
    }


    private BigDecimal scoring(ScoringDataDto scoringDataDto) {
        BigDecimal rate = calculateRate(
                scoringDataDto.getIsInsuranceEnabled(),
                scoringDataDto.getIsSalaryClient()
        );

        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED ->
                    throw new DeniedLoanException("Loan was denied. Cause: employment status does not meet established requirements.");
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.ONE);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(2));
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MANAGER -> rate = rate.subtract(BigDecimal.ONE);
            case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(3.0));
        }

        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(25))) > 0)
            throw new DeniedLoanException("Loan was denied. Cause: the possible loan amount has been exceeded.");

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3.0));
            case DIVORCED -> rate = rate.add(BigDecimal.ONE);
        }

        int age = LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear();
        if (LocalDate.now().minusYears(1).isBefore(scoringDataDto.getBirthdate()))
            age--;

        if (age > 65 || age < 20)
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");

        switch (scoringDataDto.getGender()) {
            case MALE -> {
                if (age >= 32 && age < 60)
                    rate = rate.subtract(BigDecimal.valueOf(3));
            }
            case FEMALE -> {
                if (age >= 30 && age < 55)
                    rate = rate.subtract(BigDecimal.valueOf(3));
            }
            case OTHER -> rate = rate.add(BigDecimal.valueOf(7));
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18
                || scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3)
            throw new DeniedLoanException("Loan was denied. Cause: work experience does not meet established requirements.");

        return rate;
    }

    private List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, Integer term,
                                                                  BigDecimal totalRate,
                                                                  BigDecimal monthlyPayment
    ) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        BigDecimal remainingDebt = amount;
        BigDecimal rateDay = totalRate.multiply(BigDecimal.valueOf(365));
        LocalDate date = LocalDate.now();

        for (int i = 1; i <= term; i++) {
            LocalDate datePayment = date.plusMonths(1);
            long days = ChronoUnit.DAYS.between(date, datePayment);
            BigDecimal interestPayment = remainingDebt.multiply(rateDay).multiply(BigDecimal.valueOf(days)).round(mathContextDoc);
            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(monthlyPayment);
            date = datePayment;

            PaymentScheduleElementDto scheduleElement = new PaymentScheduleElementDto();
            scheduleElement.setNumber(i);
            scheduleElement.setDate(datePayment);
            scheduleElement.setTotalPayment(monthlyPayment);
            scheduleElement.setInterestPayment(interestPayment);
            scheduleElement.setDebtPayment(debtPayment);
            scheduleElement.setRemainingDebt(remainingDebt);

            schedule.add(scheduleElement);
        }

        return schedule;
    }
}