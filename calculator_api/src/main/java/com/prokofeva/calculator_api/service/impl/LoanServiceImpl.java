package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.LoanService;
import com.prokofeva.calculator_api.service.ScoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final CreditService creditService;
    private final InsuranceService insuranceService;
    private final ScoringService scoringService;

    @Override
    public List<LoanOfferDto> createListOffer(LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> offers = new ArrayList<>(List.of(
                createLoanOffer(loanStatementRequestDto, false, false),
                createLoanOffer(loanStatementRequestDto, false, true),
                createLoanOffer(loanStatementRequestDto, true, true),
                createLoanOffer(loanStatementRequestDto, true, false)
        ));
        offers.sort((of1, of2) -> of2.getRate().compareTo(of1.getRate()));

        return offers;
    }

      private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto,
                                         boolean isInsuranceEnabled, boolean isSalaryClient) {

        BigDecimal amount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();
        BigDecimal rate = scoringService.calculateRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal insurance = isInsuranceEnabled
                ? insuranceService.calculateInsurance(amount, term)
                : BigDecimal.ZERO;
        BigDecimal monthlyPayment = creditService.calculateMonthlyPayment(amount, term, rate);
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term)).add(insurance); //todo через график было бы точнее

        LoanOfferDto offerDto = new LoanOfferDto();         //todo может в builder?
        offerDto.setStatementId(UUID.randomUUID());
        offerDto.setRequestedAmount(amount);
        offerDto.setTotalAmount(totalAmount);
        offerDto.setTerm(term);
        offerDto.setMonthlyPayment(monthlyPayment);
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }

}
