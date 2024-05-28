package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.ValidDtoFactory;
import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculatorServiceImplTest {

    @Mock
    private LoanService loanService;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CalculatorServiceImpl calculatorService;


    @Test
    void createListOffer() {
        calculatorService.setPrescoringMinAge(18);
        LoanStatementRequestDto requestDto =
                (LoanStatementRequestDto) ValidDtoFactory.getDto(LoanStatementRequestDto.class);

        when(loanService.createListOffer(requestDto))
                .thenReturn(List.of(new LoanOfferDto(), new LoanOfferDto(),
                        new LoanOfferDto(), new LoanOfferDto()));
        List<LoanOfferDto> loanOfferDtoList = calculatorService.createListOffer(requestDto);
        verify(loanService, times(1)).createListOffer(any());
        assertNotNull(loanOfferDtoList);
        assertEquals(4, loanOfferDtoList.size());

        //   assertEquals(requestDto.getAmount(), loanOfferDtoList.get(0).getRequestedAmount());
        //   assertEquals(requestDto.getAmount(), loanOfferDtoList.get(1).getRequestedAmount());
        //   assertEquals(requestDto.getAmount(), loanOfferDtoList.get(2).getRequestedAmount());
        //   assertEquals(requestDto.getAmount(), loanOfferDtoList.get(3).getRequestedAmount());
//
        //   assertNotEquals(loanOfferDtoList.get(0).getStatementId(), loanOfferDtoList.get(1).getStatementId());
        //   assertNotEquals(loanOfferDtoList.get(1).getStatementId(), loanOfferDtoList.get(2).getStatementId());
        //   assertNotEquals(loanOfferDtoList.get(2).getStatementId(), loanOfferDtoList.get(3).getStatementId());
//
        //   assertTrue(loanOfferDtoList.get(0).getTotalAmount().compareTo(loanOfferDtoList.get(0).getRequestedAmount()) > 0);
        //   assertTrue(loanOfferDtoList.get(1).getTotalAmount().compareTo(loanOfferDtoList.get(1).getRequestedAmount()) > 0);
        //   assertTrue(loanOfferDtoList.get(2).getTotalAmount().compareTo(loanOfferDtoList.get(2).getRequestedAmount()) > 0);
        //   assertTrue(loanOfferDtoList.get(3).getTotalAmount().compareTo(loanOfferDtoList.get(3).getRequestedAmount()) > 0);
    }

    @Test
    public void createListOfferFail() {
        LoanStatementRequestDto requestDto =
                (LoanStatementRequestDto) ValidDtoFactory.getDto(LoanStatementRequestDto.class);

        requestDto.setBirthdate(LocalDate.of(2022, 2, 21));
        calculatorService.setPrescoringMinAge(18);
        List<LoanOfferDto> loanOfferDtoList = null;

        try {
            loanOfferDtoList = calculatorService.createListOffer(requestDto);
        } catch (Exception e) {
            assertNull(loanOfferDtoList);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: age does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
            verify(loanService, times(0)).createListOffer(any());
        }
    }

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = (ScoringDataDto) ValidDtoFactory.getDto(ScoringDataDto.class);
        CreditDto creditDto = (CreditDto) ValidDtoFactory.getDto(CreditDto.class);
        when(creditService.calculateCredit(scoringDataDto)).thenReturn(creditDto);

        CreditDto testCreditDto = calculatorService.calculateCredit(scoringDataDto);

        verify(creditService, times(1)).calculateCredit(scoringDataDto);
        assertEquals(creditDto, testCreditDto);
    }
}