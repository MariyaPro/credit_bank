package com.prokofeva.statement_api.service.impl;

import com.prokofeva.statement_api.doman.LoanOfferDto;
import com.prokofeva.statement_api.doman.LoanStatementRequestDto;
import com.prokofeva.statement_api.exception.ExternalServiceException;
import com.prokofeva.statement_api.feign.DealFeignClient;
import com.prokofeva.statement_api.service.StatementService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final DealFeignClient dealFeignClient;
    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> listOffers;
        try {
            listOffers = dealFeignClient.getListOffers(loanStatementRequestDto);
            log.info("Получен ответ от внешнего сервиса ({}statement).", dealFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + dealFeignClientUrl + "offer/select).";
            log.info(message);
            throw new ExternalServiceException(message);
        }

        return listOffers;
    }

    @Override
    public void selectAppliedOffer(LoanOfferDto loanOfferDto) {

    }
}
