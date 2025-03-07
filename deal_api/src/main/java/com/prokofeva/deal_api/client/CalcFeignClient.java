package com.prokofeva.deal_api.client;

import com.prokofeva.dto.CreditDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.dto.ScoringDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "calc", url = "${calc_feignclient_url}")
public interface CalcFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/offers", consumes = "application/json", produces = "application/json")
    List<LoanOfferDto> getListOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "/calc", consumes = "application/json", produces = "application/json")
    CreditDto calculateCredit(@RequestBody ScoringDataDto scoringDataDto);
}