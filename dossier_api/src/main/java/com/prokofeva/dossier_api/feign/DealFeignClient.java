package com.prokofeva.dossier_api.feign;

import com.prokofeva.dto.StatementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "deal", url = "${deal_feignclient_url}")
public interface DealFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/admin/statement/{statementId}", consumes = "application/json", produces = "application/json")
    StatementDto getStatementDto(@PathVariable String statementId);
}
