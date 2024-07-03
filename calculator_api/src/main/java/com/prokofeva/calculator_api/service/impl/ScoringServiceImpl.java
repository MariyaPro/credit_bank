package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.dto.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringServiceImpl implements ScoringService {
    @Value("${rate_loan.base}")
    private BigDecimal rateLoanBase;

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
    private BigDecimal correctionInsuranceEnabled;

    @Value("${rate_loan.correction.salary_client}")
    private BigDecimal correctionSalaryClient;

    @Value("${rate_loan.correction.gender.other}")
    private BigDecimal correctionGenderOther;

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

    @Override
    public BigDecimal calculateRate(boolean isInsuranceEnabled, boolean isSalaryClient, String logId) {
        log.info("{} -- Базовая ставка по кредиту составляет: {}.", logId, rateLoanBase);
        BigDecimal rate = rateLoanBase;
        if (isInsuranceEnabled) {
            rate = rate.add(correctionInsuranceEnabled);
            log.info("{} -- Ставка скорректирована (isInsuranceEnabled: поправка: {}).", logId, correctionInsuranceEnabled);
        }
        if (isSalaryClient) {
            rate = rate.add(correctionSalaryClient);
            log.info("{} -- Ставка скорректирована (isSalaryClient: поправка: {}).", logId, correctionSalaryClient);
        }
        return rate;
    }

    @Override
    public BigDecimal scoring(ScoringDataDto scoringDataDto, String logId) {
        log.info("{} -- Скоринг заявки. Клиент: {} {} {}, сумма: {}, срок {}, страховка: {}.", logId
                , scoringDataDto.getFirstName(), scoringDataDto.getMiddleName(), scoringDataDto.getLastName()
                , scoringDataDto.getAmount(), scoringDataDto.getTerm(), scoringDataDto.getIsInsuranceEnabled());
        BigDecimal rate = calculateRate(
                scoringDataDto.getIsInsuranceEnabled(),
                scoringDataDto.getIsSalaryClient(),
                logId);

        switch (scoringDataDto.getEmployment().getStatus()) {
            case UNEMPLOYED ->
                    throw new DeniedLoanException("Loan was denied. Cause: employment status does not meet established requirements.");
            case SELF_EMPLOYED -> {
                rate = rate.add(correctionSelfEmployed);
                log.info("{} -- Ставка скорректирована (EmploymentStatus: SELF_EMPLOYED, поправка: {}).", logId, correctionSelfEmployed);
            }
            case BUSINESS_OWNER -> {
                rate = rate.add(correctionBusinessOwner);
                log.info("{} -- Ставка скорректирована (EmploymentStatus: BUSINESS_OWNER, поправка: {}).", logId, correctionBusinessOwner);
            }
        }

        switch (scoringDataDto.getEmployment().getPosition()) {
            case MID_MANAGER -> {
                rate = rate.add(correctionManager);
                log.info("{} -- Ставка скорректирована (EmploymentPosition: MANAGER, поправка: {}).", logId, correctionManager);

            }
            case TOP_MANAGER -> {
                rate = rate.add(correctionTopManager);
                log.info("{} -- Ставка скорректирована (EmploymentPosition: TOP_MANAGER, поправка: {}).", logId, correctionTopManager);
            }
        }

        if (scoringDataDto.getAmount().compareTo(scoringDataDto.getEmployment().getSalary().multiply(maxNumberSalaries)) > 0)
            throw new DeniedLoanException("Loan was denied. Cause: the possible loan amount has been exceeded.");

        switch (scoringDataDto.getMaritalStatus()) {
            case MARRIED -> {
                rate = rate.add(correctionMarried);
                log.info("{} -- Ставка скорректирована (MaritalStatus: MARRIED, поправка: {}).", logId, correctionMarried);
            }
            case DIVORCED -> {
                rate = rate.add(correctionDivorced);
                log.info("{} -- Ставка скорректирована (MaritalStatus: DIVORCED, поправка: {}).", logId, correctionDivorced);
            }
        }

        int age = LocalDate.now().getYear() - scoringDataDto.getBirthdate().getYear();
        if (LocalDate.now().minusYears(1).isBefore(scoringDataDto.getBirthdate()))
            age--;

        if (age >= scoringAgeMax || age < scoringAgeMin)
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");

        switch (scoringDataDto.getGender()) {
            case MALE -> {
                if (age >= ageMinMale && age < ageMaxMale) {
                    rate = rate.add(correctionAgeMale);
                    log.info("{} -- Ставка скорректирована (Gender: MALE, {} <= возраст < {}, поправка: {}).", logId, ageMinMale, ageMaxMale, correctionAgeMale);
                }
            }
            case FEMALE -> {
                if (age >= ageMinFemale && age < ageMaxFemale) {
                    rate = rate.add(correctionAgeFemale);
                    log.info("{} -- Ставка скорректирована (Gender: FEMALE, {} <= возраст < {}, поправка: {}).", logId, ageMinFemale, ageMaxFemale, correctionAgeMale);
                }
            }
            case NON_BINARY -> {
                rate = rate.add(correctionGenderOther);
                log.info("{} -- Ставка скорректирована (Gender: OTHER, поправка: {}).", logId, correctionAgeMale);
            }
        }

        if (scoringDataDto.getEmployment().getWorkExperienceTotal() < minWorkExperienceTotal
                || scoringDataDto.getEmployment().getWorkExperienceCurrent() < minWorkExperienceCurrent)
            throw new DeniedLoanException("Loan was denied. Cause: work experience does not meet established requirements.");

        log.info("{} -- Итоговая ставка по кредиту составляет: {}", logId, rate);

        return rate;
    }
}
