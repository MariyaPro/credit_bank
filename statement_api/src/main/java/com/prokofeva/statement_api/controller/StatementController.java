package com.prokofeva.statement_api.controller;

import com.prokofeva.statement_api.model.LoanOfferDto;
import com.prokofeva.statement_api.model.LoanStatementRequestDto;
import com.prokofeva.statement_api.service.StatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(description = "Прескоринг + запрос на расчёт возможных условий кредита.")
    @PostMapping("/")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        String logId = UUID.randomUUID().toString();
        log.info("{} -- Поступила заявка на расчет вариантов займа. {}",logId, loanStatementRequestDto);
        return ResponseEntity.ok(statementService.createListOffer(loanStatementRequestDto,logId));
    }

    @PostMapping("/offer")
    @Operation(description = "Выбор одного из предложенных вариантов.")
    public ResponseEntity<Void> selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        String logId = loanOfferDto.getStatementId().toString();
        log.info("{} -- Поступила заявка на кредит. {}",logId, loanOfferDto);
        statementService.selectAppliedOffer(loanOfferDto,logId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
