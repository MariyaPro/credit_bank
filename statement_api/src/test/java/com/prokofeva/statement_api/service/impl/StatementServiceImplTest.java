package com.prokofeva.statement_api.service.impl;

import com.prokofeva.statement_api.exception.DeniedLoanException;
import com.prokofeva.statement_api.exception.ExternalServiceException;
import com.prokofeva.statement_api.feign.DealFeignClient;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void createListOffer() {
        statementService.setPrescoringMinAge(prescoringMinAge);
        statementService.setDealFeignClientUrl(dealFeignClientUrl);

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
    void createListOfferFailPrescoring() {
        statementService.setPrescoringMinAge(prescoringMinAge);
        statementService.setDealFeignClientUrl(dealFeignClientUrl);

        LoanStatementRequestDto loanRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(12)
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .email("mail.example@gmail.com")
                .birthdate(LocalDate.of(2020, 2, 21))
                .passportSeries("1234")
                .passportNumber("123456")
                .build();

        List<LoanOfferDto> loanOfferDtoList = null;
        try {
            loanOfferDtoList = statementService.createListOffer(loanRequestDto, "logId");
        } catch (Exception e) {
            assertNull(loanOfferDtoList);
            assertEquals(DeniedLoanException.class, e.getClass());
            assertArrayEquals(e.getMessage().getBytes(StandardCharsets.UTF_8)
                    , "Loan was denied. Cause: age does not meet established requirements.".getBytes(StandardCharsets.UTF_8));
        }
        assertNull(loanOfferDtoList);
    }

    @Test
    void createListOfferFailFeign() {
        statementService.setPrescoringMinAge(prescoringMinAge);
        statementService.setDealFeignClientUrl(dealFeignClientUrl);

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
        FeignException feignException = FeignException.errorStatus("dealFeignClientUrl.getListOffers",
                Response.builder()
                        .body("Test error message".getBytes())
                        .status(406)
                        .request(Request.create(Request.HttpMethod.POST, "ii", Map.of(), null, null, null))
                        .build());

        when(dealFeignClient.getListOffers(any(LoanStatementRequestDto.class))).thenThrow(feignException);

        Exception e = assertThrows(ExternalServiceException.class, () -> statementService.createListOffer(loanRequestDto, anyString()));

        assertNotNull(e);
        assertArrayEquals("Test error message".getBytes(StandardCharsets.UTF_8),
                e.getMessage().getBytes(StandardCharsets.UTF_8));

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