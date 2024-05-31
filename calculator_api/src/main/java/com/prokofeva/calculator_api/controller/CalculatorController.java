package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.LoanOfferDto;
import com.prokofeva.calculator_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/calculator")
@Tag(name = "Расчет кредитных условий по заданным параметрам.", description = "Производит предварительный и полный расчет кредита.")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @PostMapping("/offers")
    @Operation(description = "Расчёт возможных условий кредита")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("Поступила заявка на расчет вариантов займа. {}", loanStatementRequestDto.toString());
        return ResponseEntity.ok(calculatorService.createListOffer(loanStatementRequestDto));
    }


    @PostMapping("/calc")
    @Operation(description = "Валидация присланных данных + полный расчет параметров кредита ")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {
        log.info("Поступила заявка на кредит. {}", scoringDataDto.toString());
        return ResponseEntity.ok(calculatorService.calculateCredit(scoringDataDto));
    }

}