package com.prokofeva.gateway_api.feign;

import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.StatementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "deal", url = "${deal_feignclient_url}")
public interface DealFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/admin/statement/")
    List<StatementDto> getAllStatements();

    @RequestMapping(method = RequestMethod.GET, value = "/admin/statement/{statementId}")
    StatementDto getStatementById(@PathVariable String statementId);

    @RequestMapping(method = RequestMethod.POST, value = "/calculate/{statementId}", consumes = "application/json", produces = "application/json")
    ResponseEntity<Void> registrationCredit(@RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                            @PathVariable String statementId);
}