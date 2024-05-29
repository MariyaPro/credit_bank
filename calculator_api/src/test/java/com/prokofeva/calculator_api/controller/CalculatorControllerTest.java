package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.doman.LoanOfferDto;
import com.prokofeva.calculator_api.doman.LoanStatementRequestDto;
import com.prokofeva.calculator_api.service.CalculatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {
    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController controller;

    @Test
    void createLoanOffer() {
        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();
        when(calculatorService.createListOffer(requestDto))
                .thenReturn(List.of(
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto()
                ));

        ResponseEntity<List<LoanOfferDto>> response = controller.createLoanOffer(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(4, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void createLoanOffer_NotValid() {
        // запрашиваемая сумма меньше минимальной

        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();
        requestDto.setAmount(BigDecimal.valueOf(20));


        ResponseEntity<List<LoanOfferDto>> response = controller.createLoanOffer(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
//        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);

        // assertEquals(4, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void calculateCredit() {
    }
}