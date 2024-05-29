package com.prokofeva.calculator_api.service.impl;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.doman.CreditDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.ScoringDataDto;
import com.prokofeva.calculator_api.exceptions.DeniedLoanException;
import com.prokofeva.calculator_api.service.CreditService;
import com.prokofeva.calculator_api.service.OfferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@TestPropertySource("application-test.yaml")
public class CalculatorServiceImplTest {

    @Mock
    private OfferService offerService;

    @Mock
    private CreditService creditService;

    @InjectMocks
    private CalculatorServiceImpl calculatorService;

    @Test
    void createListOffer() {

        calculatorService.setPrescoringMinAge(18); //todo не цепляется из application-test.yaml

        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();

        when(offerService.createOffer(any(), anyBoolean(), anyBoolean())).thenReturn(CreatorValidDto.createLoanOfferDto());

        List<LoanOfferDto> loanOfferDtoList = calculatorService.createListOffer(requestDto);

        assertNotNull(loanOfferDtoList);
        assertEquals(4, loanOfferDtoList.size());

        verify(offerService, times(1)).createOffer(requestDto, true, false);
        verify(offerService, times(1)).createOffer(requestDto, false, false);
        verify(offerService, times(1)).createOffer(requestDto, false, true);
        verify(offerService, times(1)).createOffer(requestDto, true, true);
    }

    @Test
    public void createListOfferFail() {
        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();

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
        }
    }

    @Test
    void calculateCredit() {
        ScoringDataDto scoringDataDto = CreatorValidDto.createScoringDataDto();
        CreditDto creditDto = CreatorValidDto.createCreditDto();
        when(creditService.calculateCredit(scoringDataDto)).thenReturn(creditDto);

        CreditDto testCreditDto = calculatorService.calculateCredit(scoringDataDto);

        verify(creditService, times(1)).calculateCredit(scoringDataDto);
        assertEquals(creditDto, testCreditDto);
    }
}