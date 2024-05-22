package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/calculator")
public class CalculatorController {
    private CalculatorService calculatorService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> offers = calculatorService.createLoanOffer(loanStatementRequestDto);
        return ResponseEntity.ok(offers);
    }


    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateLoan(@RequestBody @Valid ScoringDataDto scoringDataDto) {

        return calculatorService.calculateLoan(scoringDataDto);
    }


}