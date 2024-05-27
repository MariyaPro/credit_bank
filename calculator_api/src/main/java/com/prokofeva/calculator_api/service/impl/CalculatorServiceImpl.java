package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.service.CalculatorService;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Setter
public class CalculatorServiceImpl implements CalculatorService {
    @Value("${prescoring.min_age}")
    private Integer prescoringMinAge;

    private final LoanService loanService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        if (!prescoring(loanStatementRequestDto.getBirthdate())) {
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }

        return loanService.createListOffer(loanStatementRequestDto);
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {
        return creditService.calculateCredit(scoringDataDto);
    }

    private boolean prescoring(LocalDate birthdate) {
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
    }
}