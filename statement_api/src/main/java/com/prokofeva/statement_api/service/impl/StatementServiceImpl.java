package com.prokofeva.statement_api.service.impl;

import com.prokofeva.statement_api.exception.DeniedLoanException;
import com.prokofeva.statement_api.exception.ExternalServiceException;
import com.prokofeva.statement_api.feign.DealFeignClient;
import com.prokofeva.statement_api.model.LoanOfferDto;
import com.prokofeva.statement_api.model.LoanStatementRequestDto;
import com.prokofeva.statement_api.service.StatementService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Setter
public class StatementServiceImpl implements StatementService {
    private final DealFeignClient dealFeignClient;
    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;
    @Value("${prescoring_min_age}")
    private Integer prescoringMinAge;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId) {
        log.info("{} -- Перед расчетом возможных вариантов заявка должна пройти прескоринг.", logId);
        if (!prescoring(loanStatementRequestDto.getBirthdate(), logId)) {
            log.info("{} -- Заявка не прошла прескоринг.", logId);
            throw new DeniedLoanException("Loan was denied. Cause: age does not meet established requirements.");
        }
        List<LoanOfferDto> listOffers;
        try {
            listOffers = dealFeignClient.getListOffers(loanStatementRequestDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}statement).", logId, dealFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + dealFeignClientUrl + "statement).";
            log.error("{} -- {}", logId, message);
            throw new ExternalServiceException(message);
        }

        return listOffers;
    }

    @Override
    public void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId) {
        try {
            dealFeignClient.selectAppliedOffer(loanOfferDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}statement).", logId, dealFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + dealFeignClientUrl + "offer/select).";
            log.error("{} -- {}", logId, message);
            throw new ExternalServiceException(message);
        }
    }

    private boolean prescoring(LocalDate birthdate, String logId) {
        log.info("{} -- прескоринг...", logId);
        return LocalDate.now().minusYears(prescoringMinAge).isAfter(birthdate);
    }
}
