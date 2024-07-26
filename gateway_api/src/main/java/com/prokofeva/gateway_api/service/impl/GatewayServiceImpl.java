package com.prokofeva.gateway_api.service.impl;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.gateway_api.feign.DealFeignClient;
import com.prokofeva.gateway_api.feign.StatementFeignClient;
import com.prokofeva.gateway_api.service.GatewayService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GatewayServiceImpl implements GatewayService {
    private final DealFeignClient dealFeignClient;
    private final StatementFeignClient statementFeignClient;
    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;
    @Value("${statement_feignclient_url}")
    private String statementFeignClientUrl;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto, String logId) {
        List<LoanOfferDto> listOffers = null;
        try {
            listOffers = statementFeignClient.getListOffers(loanStatementRequestDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}).", logId, statementFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + statementFeignClientUrl + ").";
            log.error("{} -- {}", logId, message);
        }
        return listOffers;
    }

    @Override
    public void selectAppliedOffer(LoanOfferDto loanOfferDto, String logId) {
        try {
            statementFeignClient.selectAppliedOffer(loanOfferDto);
            log.info("{} -- Получен ответ от внешнего сервиса ({}statement).", logId, statementFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + statementFeignClientUrl + "offer).";
            log.error("{} -- {}", logId, message);
        }
    }

    @Override
    public void registrationCredit(FinishRegistrationRequestDto finishRegistrationRequestDto, String statementId, String logId) {
        try {
            dealFeignClient.registrationCredit(finishRegistrationRequestDto, statementId);
            log.info("{} -- Получен ответ от внешнего сервиса ({}statement).", logId, dealFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + dealFeignClientUrl + "calculate/" + statementId + ").";
            log.error("{} -- {}", logId, message);
        }
    }
}
