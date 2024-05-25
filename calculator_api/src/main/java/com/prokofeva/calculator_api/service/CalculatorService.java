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

    @Value("${rate_loan.base}")
    private Double rateLoanBase;

    @Value("${rate_loan.age.min.male}")
    private Integer ageMinMale;

    @Value("${rate_loan.age.min.female}")
    private Integer ageMinFemale;

    @Value("${rate_loan.age.max.male}")
    private Integer ageMaxMale;

    @Value("${rate_loan.age.max.female}")
    private Integer ageMaxFemale;

    @Value("${rate_loan.correction.age.male}")
    private BigDecimal correctionAgeMale;

    @Value("${rate_loan.correction.age.female}")
    private BigDecimal correctionAgeFemale;

    @Value("${rate_loan.correction.employment.status.self_employed}")
    private BigDecimal correctionSelfEmployed;

    @Value("${rate_loan.correction.employment.status.business_owner}")
    private BigDecimal correctionBusinessOwner;

    @Value("${rate_loan.correction.employment.position.manager}")
    private BigDecimal correctionManager;

    @Value("${rate_loan.correction.employment.position.top_manager}")
    private BigDecimal correctionTopManager;

    @Value("${rate_loan.correction.marital_status.married}")
    private BigDecimal correctionMarried;

    @Value("${rate_loan.correction.marital_status.divorced}")
    private BigDecimal correctionDivorced;

    @Value("${rate_loan.correction.insurance_enabled}")
    private Double correctionInsuranceEnabled;

    @Value("${rate_loan.correction.salary_client}")
    private Double correctionSalaryClient;

    @Value("${rate_loan.correction.gender.other}")
    private BigDecimal correctionGenderOther;

    @Value("${rate_insurance.base}")
    private Double rateInsuranceBase;

    @Value("${rate_insurance.term.short}")
    private Integer rateInsuranceTermShort;

    @Value("${rate_insurance.term.long}")
    private Integer rateInsuranceTermLong;

    @Value("${rate_insurance.amount_loan.small}")
    private BigDecimal rateInsuranceAmountSmall;

    @Value("${rate_insurance.amount_loan.big}")
    private BigDecimal rateInsuranceAmountBig;

    @Value("${rate_insurance.correction.short_term}")
    private Double rateInsuranceCorrectionShortTerm;

    @Value("${rate_insurance.correction.long_term}")
    private Double rateInsuranceCorrectionLongTerm;

    @Value("${rate_insurance.correction.small_amount}")
    private Double rateInsuranceCorrectionSmallAmount;

    @Value("${rate_insurance.correction.big_amount}")
    private Double rateInsuranceCorrectionBigAmount;

    @Value("${prescoring.min_age}")
    private Integer prescoringMinAge;

    @Value("${scoring.age.min}")
    private Integer scoringAgeMin;

    @Value("${scoring.age.max}")
    private Integer scoringAgeMax;

    @Value("${scoring.min_work_experience.total}")
    private Integer minWorkExperienceTotal;

    @Value("${scoring.min_work_experience.current}")
    private Integer minWorkExperienceCurrent;

    @Value("${scoring.max_number_of_salaries_in_amount}")
    private BigDecimal maxNumberSalaries;

    private final MathContext mathContextDoc = new MathContext(2);
    private final MathContext mathContextCalc = new MathContext(10);

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
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));

        return offers;
    }

    private boolean prescoring(LocalDate birthdate) {
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
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
        offerDto.setTotalAmount(psk); // todo сумма или %
        offerDto.setTerm(term);
        offerDto.setMonthlyPayment(monthlyPayment);
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }

    private BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        Double rate = rateLoanBase;
        if (isInsuranceEnabled) {
            rate +=correctionInsuranceEnabled;
        }
        if (isSalaryClient) {
            rate += correctionSalaryClient;
        }
        return BigDecimal.valueOf(rate);
    }

    private BigDecimal calculateInsurance(BigDecimal amount, Integer term) {
        double rate = rateInsuranceBase;
        if (term < rateInsuranceTermShort) rate += rateInsuranceCorrectionShortTerm;
        if (term >= rateInsuranceTermLong) rate -= rateInsuranceCorrectionLongTerm;

        if (amount.compareTo(rateInsuranceAmountSmall) <= 0) rate += rateInsuranceCorrectionSmallAmount;
        if (amount.compareTo(rateInsuranceAmountBig) >= 0) rate += rateInsuranceCorrectionBigAmount;

        BigDecimal amountInsurance = amount.divide(BigDecimal.valueOf(rate * 0.01),mathContextCalc).multiply(BigDecimal.valueOf(term / 12.0));

        return amountInsurance;
    }

    // monthlyPayment = amount * rate / 12 * (1+rate/12)^term / ((1+rate/12)^term - 1)
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term, BigDecimal rate) {
        BigDecimal rateMonth = rate.divide(BigDecimal.valueOf(12), mathContextCalc);
        BigDecimal helper = rateMonth.add(BigDecimal.ONE).pow(term);        //todo: name? helper = (1+rate/12)^term

        BigDecimal monthlyPayment = amount.multiply(rateMonth).multiply(helper).divide(helper.subtract(BigDecimal.ONE), mathContextDoc);

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
            case SELF_EMPLOYED -> rate = rate.add(correctionSelfEmployed);
            case BUSINESS_OWNER -> rate = rate.add(correctionBusinessOwner);
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MANAGER -> rate = rate.add(correctionManager);
            case TOP_MANAGER -> rate = rate.add(correctionTopManager);
        }

        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(maxNumberSalaries,mathContextCalc)) > 0)
            throw new DeniedLoanException("Loan was denied. Cause: the possible loan amount has been exceeded.");

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> rate = rate.add(correctionMarried);
            case DIVORCED -> rate = rate.add(correctionDivorced);
        }

        int age = LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear();
        if (LocalDate.now().minusYears(1).isBefore(scoringDataDto.getBirthdate()))
            age--;

        if (age >= scoringAgeMax || age < scoringAgeMin)
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");

        switch (scoringDataDto.getGender()) {
            case MALE -> {
                if (age >= ageMinMale && age < ageMaxMale)
                    rate = rate.add(correctionAgeMale);
            }
            case FEMALE -> {
                if (age >= ageMinFemale && age < ageMaxFemale)
                    rate = rate.add(correctionAgeFemale);
            }
            case OTHER -> rate = rate.add(correctionGenderOther);
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < minWorkExperienceTotal
                || scoringDataDto.getEmployment().getWorkExperienceCurrent() < minWorkExperienceCurrent)
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