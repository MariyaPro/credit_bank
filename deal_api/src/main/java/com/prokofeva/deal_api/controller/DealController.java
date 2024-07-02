package com.prokofeva.deal_api.controller;

import com.prokofeva.deal_api.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.dto.LoanOfferDto;
import com.prokofeva.deal_api.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 POST: /deal/statement - расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>
 POST: /deal/offer/select - Выбор одного из предложений. Request LoanOfferDto, response void.
 POST: /deal/calculate/{statementId} - завершение регистрации + полный подсчёт кредита. Request - FinishRegistrationRequestDto, param - String, response void.

 POST: /deal/document/{statementId}/send - запрос на отправку документов.
 POST: /deal/document/{statementId}/sign - запрос на подписание документов
 POST: /deal/document/{statementId}/code - подписание документов
 */

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Deal", description = "Обрабатывает и регистрирует поступившие заявки на кредит.")
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;

    @PostMapping("/statement")
    @Operation(description = "Paсчет возможных условий кредита.")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        log.info("{} -- Получена заявка на расчет вариантов займа: {}", loanStatementRequestDto.hashCode(), loanStatementRequestDto);
        return ResponseEntity.ok(dealService.getListOffers(loanStatementRequestDto));
    }

    @PostMapping("/offer/select")
    @Operation(description = "Выбор одного из кредитных предложений.")
    public ResponseEntity<Void> selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        log.info("{} -- Клиент выбрал вариант кредита: {}.", loanOfferDto.getStatementId(), loanOfferDto);
        dealService.selectAppliedOffer(loanOfferDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(description = "Завершение регистрации и полный подсчёт кредита.")
    public ResponseEntity<Void> registrationCredit(@RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   @PathVariable String statementId) {
        log.info("{} -- Процедура регистрации кредита в базе данных. Дополнительные сведени: {}",statementId,finishRegistrationRequestDto);
        dealService.registrationCredit(finishRegistrationRequestDto, statementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/send")
    @Operation(description = "Запрос на отправку документов.")
    public ResponseEntity<Void> sendDocuments(@PathVariable String statementId) {
        log.info("{} -- Запрос на отправку документов.",statementId);
        dealService.sendDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/sign")
    @Operation(description = "Запрос на подписание документов.")
    public ResponseEntity<Void> signDocuments(@PathVariable String statementId) {
        log.info("{} -- Запрос на подписание документов.",statementId);
        dealService.signDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/code")
    @Operation(description = "Подписание документов.")
    public ResponseEntity<Void> codeDocuments(@PathVariable String statementId) {
        log.info("{} -- Подписание документов.",statementId);
        dealService.codeDocuments(statementId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
