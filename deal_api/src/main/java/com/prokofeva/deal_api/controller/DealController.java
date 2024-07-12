package com.prokofeva.deal_api.controller;

import com.prokofeva.deal_api.service.DealService;
import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.StatementDto;
import com.prokofeva.enums.ApplicationStatus;
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
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "Deal", description = "Обрабатывает и регистрирует поступившие заявки на кредит.")
@RequestMapping("/deal")
public class DealController {
    private final DealService dealService;

    @PostMapping("/statement")
    @Operation(description = "Расчет возможных условий кредита.")
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Получена заявка на расчет вариантов займа: {}", logId, loanStatementRequestDto);
        return ResponseEntity.ok(dealService.getListOffers(loanStatementRequestDto,logId));
    }

    @PostMapping("/offer/select")
    @Operation(description = "Выбор одного из кредитных предложений.")
    public ResponseEntity<Void> selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Клиент выбрал вариант кредита: {}.", logId, loanOfferDto);
        dealService.selectAppliedOffer(loanOfferDto,logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(description = "Завершение регистрации и полный подсчёт кредита.")
    public ResponseEntity<Void> registrationCredit(@RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                                   @PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Процедура регистрации кредита в базе данных. Дополнительные сведения: {}", logId, finishRegistrationRequestDto);
        dealService.registrationCredit(finishRegistrationRequestDto, statementId,logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/send")
    @Operation(description = "Запрос на отправку документов.")
    public ResponseEntity<Void> sendDocuments(@PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Запрос на отправку документов (заявка id={}).", logId, statementId);
        log.info("отработал метод POST");
        dealService.updateStatementStatus(ApplicationStatus.PREPARE_DOCUMENTS, statementId, logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/sign")
    @Operation(description = "Запрос на подписание документов.")
    public ResponseEntity<Void> signDocuments(@PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Поступил запрос на подписание документов (заявка id={}).", logId, statementId);

        dealService.signDocuments(statementId,logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/document/{statementId}/code")
    @Operation(description = "Подписание документов.")
    public ResponseEntity<Void> checkSesCode(@RequestBody String sesCode, @PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Получен ses-код от клиента (заявка id={}).", logId, statementId);
        dealService.checkSesCode(sesCode, statementId, logId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/statement/{statementId}")
    @Operation(description = "Получить заявку по id.")
    public ResponseEntity<StatementDto> getStatement(@PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Поступил запрос на получение заяки id = {}.", logId, statementId);

        return ResponseEntity.ok(dealService.getStatement(statementId, logId));
    }

    @PutMapping("/admin/statement/{statementId}/status")
    @Operation(description = "Обновить статус заявки.")
    public ResponseEntity<Void> updateStatementStatus(@RequestBody ApplicationStatus status, @PathVariable String statementId) {
        String logId = String.valueOf(UUID.randomUUID());
        log.info("{} -- Требуется изменить статус заявки (id={}) на {}.", logId, statementId, status.getValue());
        dealService.updateStatementStatus(status, statementId, logId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}