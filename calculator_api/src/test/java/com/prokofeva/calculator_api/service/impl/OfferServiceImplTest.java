package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.ScoringService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private CreditService creditService;

    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Test
    void createOffer() {
        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();
        when(scoringService.calculateRate(anyBoolean(), anyBoolean(), anyString())).thenReturn(BigDecimal.valueOf(19.00));
        when(creditService.calculateMonthlyPayment(any(), any(), any(),anyString())).thenReturn(BigDecimal.valueOf(8826.14));

        LoanOfferDto loanOfferDto = offerService.createOffer(requestDto, anyBoolean(), anyBoolean(),anyString());

        assertNotNull(loanOfferDto);
        assertNull(loanOfferDto.getStatementId());
        assertEquals(requestDto.getAmount(), loanOfferDto.getRequestedAmount());
        assertEquals(requestDto.getTerm(), loanOfferDto.getTerm());
        assertEquals(BigDecimal.valueOf(8826.14), loanOfferDto.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(19.00), loanOfferDto.getRate());
        assertNotNull(loanOfferDto.getTotalAmount());
        assertTrue(loanOfferDto.getTotalAmount().compareTo(loanOfferDto.getRequestedAmount()) > 0);
    }
}