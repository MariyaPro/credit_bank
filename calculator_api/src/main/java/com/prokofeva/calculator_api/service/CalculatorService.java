package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.*;
import lombok.RequiredArgsConstructor;
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
public class CalculatorService {

    @Value("${base_loan_rate}")
    private Double baseRate;

    private final MathContext mathContextDoc = new MathContext(2);
    private final MathContext mathContextCalc = new MathContext(10);

    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        if (!prescoring(loanStatementRequestDto)) throw new RuntimeException(); // todo bag request

        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(createLoanOffer(loanStatementRequestDto, false, false));
        offers.add(createLoanOffer(loanStatementRequestDto, false, true));
        offers.add(createLoanOffer(loanStatementRequestDto, true, true));
        offers.add(createLoanOffer(loanStatementRequestDto, true, false));

        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));

        return offers;
    }

    private boolean prescoring(LoanStatementRequestDto loanStatementRequestDto) {
        LocalDate maxDate = LocalDate.now().minusYears(18);
        return maxDate.isAfter(loanStatementRequestDto.getBirthdate());
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
        Double rate = baseRate;
        if (isInsuranceEnabled) rate -= 3.0;
        if (isSalaryClient) rate -= 1.0;
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

        return amountInsurance.max(minInsurance).min(maxInsurance);
    }

    // monthlyPayment = amount * rate / 12 * (1+rate/12)^term / ((1+rate/12)^term -1)  ,
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        BigDecimal rateMonth = rate.divide(BigDecimal.valueOf(12), mathContextCalc);
        BigDecimal helper = rateMonth.add(BigDecimal.ONE).pow(term);//todo name? helper=(1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth).multiply(helper).divide(helper.add(BigDecimal.valueOf(-1)), mathContextDoc);

        return monthlyPayment;
    }

    // вроде лишнее
//    private BigDecimal calculateTotalAmount(BigDecimal amount, Integer term, BigDecimal monthlyPayment, boolean isInsuranceEnabled) {
//        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
//        if (isInsuranceEnabled) totalAmount = totalAmount.add(calculateInsurance(amount, term));
//        return totalAmount;
//    }

    private BigDecimal calculatePsk(Integer term, BigDecimal monthlyPayment, BigDecimal insurance) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term)).add(insurance);
    }

    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        BigDecimal amount = scoringDataDto.getAmount();     //todo повтор кода
        Integer term = scoringDataDto.getTerm();
        BigDecimal insurance = scoringDataDto.getIsInsuranceEnabled() ? calculateInsurance(amount, term) : BigDecimal.ZERO;     //todo повтор кода

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
        BigDecimal rate = calculateRate(scoringDataDto.getIsInsuranceEnabled()
                , scoringDataDto.getIsSalaryClient());

        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED -> throw new RuntimeException("отказ"); //todo
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.ONE);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(2));
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MANAGER -> rate = rate.add(BigDecimal.valueOf(-1.0));
            case TOP_MANAGER -> rate = rate.add(BigDecimal.valueOf(-3.0));
        }

        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(BigDecimal.valueOf(25))) > 0)
            throw new RuntimeException("отказ"); //todo

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> rate = rate.add(BigDecimal.valueOf(-3.0));
            case DIVORCED -> rate = rate.add(BigDecimal.ONE);
        }

        int age = LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear();
        if (LocalDate.now().minusYears(1).isBefore(scoringDataDto.getBirthdate()))
            age--;

        if (age > 65 || age < 20)
            throw new RuntimeException("отказ"); //todo

        switch (scoringDataDto.getGender()) {
            case MALE -> {
                if (age >= 32 && age < 60)
                    rate = rate.add(BigDecimal.valueOf(-3));
            }
            case FEMALE -> {
                if (age >= 30 && age < 55)
                    rate = rate.add(BigDecimal.valueOf(-3));
            }
            case OTHER -> rate = rate.add(BigDecimal.valueOf(7));
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < 18
                || scoringDataDto.getEmployment().getWorkExperienceCurrent() < 3)
            throw new RuntimeException("отказ"); // todo

        return rate;
    }

    private List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, Integer term,
                                                                  BigDecimal totalRate,
                                                                  BigDecimal monthlyPayment
    ) {
        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        BigDecimal remainingDebt = amount;
        BigDecimal totalPayment = monthlyPayment;
        BigDecimal rateDay = totalRate.multiply(BigDecimal.valueOf(365));
        LocalDate date = LocalDate.now();
        for (int i = 1; i < term; i++) {
            LocalDate datePayment = date.plusMonths(1);
            long days = ChronoUnit.DAYS.between(date, datePayment);
            BigDecimal interestPayment = remainingDebt.multiply(rateDay).multiply(BigDecimal.valueOf(days)).round(mathContextDoc);
// todo закончить


        }

        return schedule;
    }
}