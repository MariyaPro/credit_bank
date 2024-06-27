package com.prokofeva.statement_api.service.impl;

import com.prokofeva.statement_api.feign.DealFeignClient;
import com.prokofeva.statement_api.model.LoanOfferDto;
import com.prokofeva.statement_api.model.LoanStatementRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.yaml")
class StatementServiceImplTest {

    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;
    @Value("${prescoring_min_age}")
    private Integer prescoringMinAge;
    @InjectMocks
    private StatementServiceImpl statementService;
    @Mock
    private DealFeignClient dealFeignClient;

    @BeforeEach
    void setProperties() {
        statementService.setPrescoringMinAge(prescoringMinAge);
        statementService.setDealFeignClientUrl(dealFeignClientUrl);
    }

    @Test
    void createListOffer() {
        LoanStatementRequestDto loanRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .email("mail.example@gmail.com")
                .birthdate(LocalDate.of(2000, 2, 21))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        when(dealFeignClient.getListOffers(any(LoanStatementRequestDto.class))).thenReturn(List.of(
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build()
        ));

        List<LoanOfferDto> listOffersAc = statementService.createListOffer(loanRequestDto, anyString());

        assertNotNull(listOffersAc);
        assertEquals(4, listOffersAc.size());

        verify(dealFeignClient, times(1)).getListOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void selectAppliedOffer() {
        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(111185))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9263.45))
                .rate(BigDecimal.valueOf(20.00))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        statementService.selectAppliedOffer(loanOfferDto, "fceaf46f-08f4-462f-9267-cc03047835a5");

        verify(dealFeignClient, times(1)).selectAppliedOffer(loanOfferDto);
    }
}