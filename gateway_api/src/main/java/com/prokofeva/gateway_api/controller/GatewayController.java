package com.prokofeva.gateway_api.controller;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.StatementDto;
import com.prokofeva.gateway_api.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Gateway", description = "Главная задача этого МС - инкапсулировать сложную логику всей внутренней системы, предоставив клиенту простой и понятный API.")
@RestController
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;
    @Value("${spring.application.name}")
    private String appName;

    @Operation(description = "Получение всех заявок.")
    @GetMapping("/deal/admin/statement/")
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступил запрос на получение всех заявок.", logId);

        return ResponseEntity.ok(gatewayService.getAllStatements(logId));
    }

    @Operation(description = "Получение заявки по id.")
    @GetMapping("/deal/admin/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatementById(@PathVariable String statementId) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступил запрос на получение заявоки с id={}.", logId,statementId);

        return ResponseEntity.ok(gatewayService.getStatementById(statementId,logId));
    }

    @Operation(description = "Прескоринг + запрос на расчёт возможных условий кредита.")
    @PostMapping("/statement/")
    public ResponseEntity<List<LoanOfferDto>> createLoanOffer(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на расчет вариантов займа. {}", logId, loanStatementRequestDto);

        return ResponseEntity.ok(gatewayService.createListOffer(loanStatementRequestDto, logId));
    }

    @Operation(description = "Выбор одного из предложенных вариантов.")
    @PostMapping("/statement/offer")
    public ResponseEntity<Void> selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        String logId = String.join(":", appName, UUID.randomUUID().toString());
        log.info("{} -- Поступила заявка на кредит. {}", logId, loanOfferDto);
        gatewayService.selectAppliedOffer(loanOfferDto, logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deal/calculate/{statementId}")
    @Operation(description = "Завершение регистрации и полный подсчёт кредита.")
    public ResponseEntity<Void> registrationCredit(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                                   @PathVariable String statementId) {
        String logId = String.join(":",appName ,UUID.randomUUID().toString());
        log.info("{} -- Процедура регистрации кредита в базе данных.", logId);
        gatewayService.registrationCredit(finishRegistrationRequestDto, statementId, logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
