package com.prokofeva.calculator_api.service;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {
    private Prescoring prescoring;
    private Scoring scoring;

    public List<LoanOfferDto> createLoanOffer(LoanStatementRequestDto loanStatementRequestDto) {
        prescoring.isValid(loanStatementRequestDto);

        List<LoanOfferDto> offers = new ArrayList<>();
        offers.add(calcOffer(loanStatementRequestDto, false, false));
        offers.add(calcOffer(loanStatementRequestDto, false, true));
        offers.add(calcOffer(loanStatementRequestDto, true, true));
        offers.add(calcOffer(loanStatementRequestDto, true, false));

        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));

        return offers;
    }

    private LoanOfferDto calcOffer(LoanStatementRequestDto loanStatementRequestDto, boolean isInsuranceEnabled, boolean isSalaryClient) {
        LoanOfferDto offer = new LoanOfferDto();


        return offer;
    }

    public ResponseEntity<CreditDto> calculateLoan(ScoringDataDto scoringDataDto) {
        return null;
    }
}