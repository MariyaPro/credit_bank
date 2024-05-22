package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Setter
public class CalculatorService {

    @Value("${base_loan_rate}")
    private Double baseRate;

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
        BigDecimal rate = calculateRate(isInsuranceEnabled, isSalaryClient);

        BigDecimal totalAmount = calculateTotalAmount(loanStatementRequestDto.getAmount(),
                loanStatementRequestDto.getTerm(), rate, isInsuranceEnabled);

        LoanOfferDto offerDto = new LoanOfferDto();
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(loanStatementRequestDto.getAmount());
        offerDto.setTotalAmount(totalAmount);
        offerDto.setTerm(loanStatementRequestDto.getTerm());
        offerDto.setMonthlyPayment(totalAmount.multiply(BigDecimal.valueOf(1.0 / loanStatementRequestDto.getTerm())));
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

        BigDecimal amountInsurance = amount.multiply(BigDecimal.valueOf(rate/100.0)).multiply(BigDecimal.valueOf(term / 12.0));

        BigDecimal maxInsurance = amount.multiply(BigDecimal.valueOf(0.1));
        BigDecimal minInsurance = amount.multiply(BigDecimal.valueOf(0.05));

        return amountInsurance.max(minInsurance).min(maxInsurance);
    }

    private BigDecimal calculateTotalAmount(BigDecimal amount, Integer term, BigDecimal rate, boolean isInsuranceEnabled) {
        BigDecimal totalAmount = isInsuranceEnabled ? amount.add(calculateInsurance(amount, term)) : amount;
        totalAmount = totalAmount.add(totalAmount
                .multiply(rate)
                .multiply(BigDecimal.valueOf(term / 12.0)));
        return totalAmount;
    }

    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        BigDecimal totalRate = scoring(scoringDataDto);

        List<PaymentScheduleElementDto>

        CreditDto creditDto = new CreditDto();
        creditDto.setAmount(scoringDataDto.getAmount());
        creditDto.setTerm(scoringDataDto.getTerm());
        creditDto.setMonthlyPayment();
        creditDto.setRate(totalRate);
        creditDto.setPsk();
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule();

        return creditDto;
    }

    private BigDecimal scoring(ScoringDataDto scoringDataDto) {
        BigDecimal rate = calculateRate(scoringDataDto.getIsInsuranceEnabled()
                , scoringDataDto.getIsSalaryClient());

        switch (scoringDataDto.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED -> throw new RuntimeException("отказ"); //todo
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.ONE);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.TWO);
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
}