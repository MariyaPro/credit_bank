package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.service.CalculatorService;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Setter
public class CalculatorServiceImpl implements CalculatorService {
    @Value("${prescoring.min_age}")
    private Integer prescoringMinAge;

    private final OfferService offerService;
    private final CreditService creditService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        if (!prescoring(loanStatementRequestDto.getBirthdate())) {
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }
        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                offerService.createOffer(loanStatementRequestDto, false, false),
                offerService.createOffer(loanStatementRequestDto, false, true),
                offerService.createOffer(loanStatementRequestDto, true, true),
                offerService.createOffer(loanStatementRequestDto, true, false)
        ));
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));

        return offers;
    }

    @Override
    public CreditDto calculateCredit(ScoringDataDto scoringDataDto) {

        return creditService.calculateCredit(scoringDataDto);
    }

    private boolean prescoring(LocalDate birthdate) {
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
    }
}