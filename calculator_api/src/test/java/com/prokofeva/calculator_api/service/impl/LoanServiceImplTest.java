package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.ValidDtoFactory;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.InsuranceService;
import com.prokofeva.calculator_api.service.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private CreditService creditService;
    @Mock
    private InsuranceService insuranceService;
    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    void createListOffer() {
        LoanStatementRequestDto requestDto = (LoanStatementRequestDto) ValidDtoFactory.getDto(LoanStatementRequestDto.class);
        when(scoringService.calculateRate(anyBoolean(), anyBoolean())).thenReturn(BigDecimal.valueOf(19.00));
        when(insuranceService.calculateInsurance(any(), any())).thenReturn(BigDecimal.valueOf(1000));
        when(creditService.calculateMonthlyPayment(any(), any(), any())).thenReturn(BigDecimal.valueOf(8826.14));

        List<LoanOfferDto> listOffer = loanService.createListOffer(requestDto);

        assertNotNull(listOffer);
        assertEquals(4, listOffer.size());

        // verify(loanService,times(4)).createLoanOffer(requestDto,anyBoolean(),anyBoolean());
        //     verify(loanService,times(1)).createLoanOffer(requestDto,true,false);
        //     verify(loanService,times(1)).createLoanOffer(requestDto,false,false);
        //     verify(loanService,times(1)).createLoanOffer(requestDto,false,true);
        //     verify(loanService,times(1)).createLoanOffer(requestDto,true,true);
    }


}