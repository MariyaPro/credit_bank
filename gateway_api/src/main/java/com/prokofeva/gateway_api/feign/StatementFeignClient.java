package com.prokofeva.gateway_api.feign;

import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "statement", url = "${statement_feignclient_url}")
public interface StatementFeignClient {

    @RequestMapping(method = RequestMethod.POST, value = "/", consumes = "application/json", produces = "application/json")
    List<LoanOfferDto> getListOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "/offer", consumes = "application/json", produces = "application/json")
    void selectAppliedOffer(@RequestBody LoanOfferDto loanOfferDto);
}