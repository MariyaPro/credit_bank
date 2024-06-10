package com.prokofeva.deal_api.client;

import com.prokofeva.deal_api.doman.dto.CreditDto;
import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.deal_api.doman.dto.ScoringDataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "calc", url = "http://localhost:8090/calculator/")
public interface CalcFeignClient {
    @RequestMapping(method = RequestMethod.POST, value = "/offers", consumes = "application/json", produces = "application/json")
    List<LoanOfferDto> getListOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto);

    @RequestMapping(method = RequestMethod.POST, value = "/calc",consumes = "application/json", produces = "application/json")
    CreditDto calculateCredit(@RequestBody ScoringDataDto scoringDataDto);
}