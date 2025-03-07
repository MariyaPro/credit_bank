package com.prokofeva.statement_api.controller;

import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.statement_api.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
@Tag(name = "Statement", description = "Прескоринг + запрос на расчёт возможных условий кредита.")
@RequestMapping("/statement")
public class StatementController {
    private final StatementService statementService;
    @Value("${spring.application.name}")
    private String appName;

    @Operation(description = "Прескоринг + запрос на расчёт возможных условий кредита.")
    @PostMapping("/")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на расчет вариантов займа. {}",logId, loanStatementRequestDto);
        return ResponseEntity.ok(statementService.createListOffer(loanStatementRequestDto,logId));
    }

    @PostMapping("/offer")
    @Operation(description = "Выбор одного из предложенных вариантов.")
    public ResponseEntity<Void> selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на кредит. {}",logId, loanOfferDto);
        statementService.selectAppliedOffer(loanOfferDto,logId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}