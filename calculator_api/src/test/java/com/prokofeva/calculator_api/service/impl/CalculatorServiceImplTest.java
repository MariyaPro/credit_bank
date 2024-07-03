package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.dto.CreditDto;
import com.prokofeva.calculator_api.dto.LoanOfferDto;
import com.prokofeva.calculator_api.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceImplTest {

    @Mock
    private OfferService offerService;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    @Test
    void createListOffer() {
        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();

        when(offerService.createOffer(any(), anyBoolean(), anyBoolean(), anyString())).thenReturn(CreatorValidDto.createLoanOfferDto());

        List<LoanOfferDto> loanOfferDtoList = calculatorService.createListOffer(requestDto, "logId");

        assertNotNull(loanOfferDtoList);
        assertEquals(4, loanOfferDtoList.size());

        verify(offerService, times(1)).createOffer(requestDto, true, false, "logId");
        verify(offerService, times(1)).createOffer(requestDto, false, false, "logId");
        verify(offerService, times(1)).createOffer(requestDto, false, true, "logId");
        verify(offerService, times(1)).createOffer(requestDto, true, true, "logId");
    }

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = CreatorValidDto.createScoringDataDto();
        CreditDto creditDto = CreatorValidDto.createCreditDto();
        when(creditService.calculateCredit(scoringDataDto, "logId")).thenReturn(creditDto);

        CreditDto testCreditDto = calculatorService.calculateCredit(scoringDataDto, "logId");

        verify(creditService, times(1)).calculateCredit(scoringDataDto, "logId");
        assertEquals(creditDto, testCreditDto);
    }
}