package com.prokofeva.statement_api.feign;

import com.prokofeva.statement_api.doman.LoanOfferDto;
import com.prokofeva.statement_api.doman.LoanStatementRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "deal", url = "${deal_feignclient_url}")
public interface DealFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/statement", consumes = "application/json", produces = "application/json")
    List<LoanOfferDto> getListOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "/offer/select", consumes = "application/json", produces = "application/json")
    void calculateCredit(@RequestBody LoanOfferDto loanOfferDto);
}
