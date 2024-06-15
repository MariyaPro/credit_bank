package com.prokofeva.deal_api.controller;

import com.prokofeva.deal_api.doman.dto.FinishRegistrationRequestDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * POST: /deal/statement - расчёт возможных условий кредита. Request - LoanStatementRequestDto, response - List<LoanOfferDto>
 * POST: /deal/offer/select - Выбор одного из предложений. Request LoanOfferDto, response void.
 * POST: /deal/calculate/{statementId} - завершение регистрации + полный подсчёт кредита. Request - FinishRegistrationRequestDto, param - String, response void.
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
        return ResponseEntity.ok(dealService.getListOffers(loanStatementRequestDto));
    }

    @PostMapping("/offer/select")
    @Operation(description = "Выбор одного из кредитных предложений.")
    public void selectAppliedOffer(@RequestBody @Valid LoanOfferDto loanOfferDto) {
         dealService.selectAppliedOffer(loanOfferDto);
    }

    @PostMapping("/calculate/{statementId}")
    @Operation(description = "Завершение регистрации и полный подсчёт кредита.")
    public void registrationCredit(@RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto,
                                   @PathVariable String statementId) {
        dealService.registrationCredit(finishRegistrationRequestDto, statementId);
    }
}
