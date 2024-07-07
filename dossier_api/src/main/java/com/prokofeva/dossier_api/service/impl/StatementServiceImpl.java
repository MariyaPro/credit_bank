package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.exception.ExternalServiceException;
import com.prokofeva.dossier_api.feign.DealFeignClient;
import com.prokofeva.dossier_api.service.StatementService;
import com.prokofeva.dto.StatementDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final DealFeignClient dealFeignClient;

    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;

    @Override
    public StatementDto getInfoFromDb(UUID statementId, String logId) {
        log.info("{} -- Для формирования письма необходимо получить информацию о заявке.", statementId.toString());
        StatementDto statementDto;
        try {
            statementDto = dealFeignClient.getStatementDto(statementId.toString());
            log.info("{} -- Получен ответ от внешнего сервиса ({}statement).", logId, dealFeignClientUrl);
        } catch (FeignException e) {
            String message = e.status() == 406 ?
                    new String(e.responseBody().get().array())
                    : "Error from external service (" + dealFeignClientUrl + "statement).";
            log.error("{} -- {}", logId, message);
            throw new ExternalServiceException(message);
        }

        return statementDto;
    }
}