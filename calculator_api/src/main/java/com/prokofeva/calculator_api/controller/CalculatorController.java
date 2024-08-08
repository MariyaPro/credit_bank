package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.service.CalculatorService;
import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.ScoringDataDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Расчет кредитных условий по заданным параметрам.", description = "Производит предварительный и полный расчет кредита.")
@RequestMapping("/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;
    @Value("${spring.application.name}")
    private String appName;

    @Operation(description = "Расчёт возможных условий кредита")
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на расчет вариантов займа. {}", logId, loanStatementRequestDto.toString());
        return ResponseEntity.ok(calculatorService.createListOffer(loanStatementRequestDto, logId));
    }

    @PostMapping("/calc")
    @Operation(description = "Валидация присланных данных + полный расчет параметров кредита ")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody @Valid ScoringDataDto scoringDataDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на кредит. {}", logId, scoringDataDto.toString());
        return ResponseEntity.ok(calculatorService.calculateCredit(scoringDataDto, logId));
    }
}