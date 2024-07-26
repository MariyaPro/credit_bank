package com.prokofeva.gateway_api.controller;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.gateway_api.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "Gateway", description = "")
@RestController
@RequiredArgsConstructor
public class GatewayController {
    private final GatewayService gatewayService;
    @Value("${spring.application.name}")
    private String appName;

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
