package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Расчет кредитных условий по заданным параметрам.", description = "Производит предварительный и полный расчет кредита.")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @PostMapping("/offers")

    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {

        return ResponseEntity.ok(calculatorService.createListOffer(loanStatementRequestDto));
    }


    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {

        return ResponseEntity.ok(calculatorService.calculateCredit(scoringDataDto));
    }


}