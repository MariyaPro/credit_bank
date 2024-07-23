package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dossier_api.exception.ExternalServiceException;
import com.prokofeva.dossier_api.feign.DealFeignClient;
import com.prokofeva.dto.*;
import com.prokofeva.enums.*;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.yaml")
class StatementServiceImplTest {

    @Value("${deal_feignclient_url}")
    private String dealFeignClientUrl;
    @InjectMocks
    private StatementServiceImpl statementService;
    @Mock
    private DealFeignClient dealFeignClient;
    private static StatementDto statementDto;

    @BeforeAll
    public static void setupStatementDto() {
        EmploymentDto employmentDto = EmploymentDto.builder()
                .status(EmploymentStatus.EMPLOYED)
                .employerInn("505021556592")
                .salary(BigDecimal.valueOf(260000))
                .position(EmploymentPosition.TOP_MANAGER)
                .workExperienceTotal(300)
                .workExperienceCurrent(10)
                .build();
        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();
        ClientDto clientDto = ClientDto.builder()
                .clientId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .lastName("LastNam_clientAc")
                .firstName("FirstName_clientAc")
                .middleName("MiddleName_clientAc")
                .birthDate(LocalDate.of(2000, 2, 21))
                .email("mail.example_clientAc@gmail.com")
                .gender(Gender.MALE)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .employment(employmentDto)
                .accountNumber("accountNumber")
                .passport(passport)
                .build();
        CreditDto creditDto = CreditDto.builder()
                .amount(BigDecimal.valueOf(50000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(8826.14))
                .rate(BigDecimal.valueOf(20.00))
                .psk(BigDecimal.valueOf(19.974))
                .insuranceEnabled(false)
                .salaryClient(false)
                .paymentSchedule(List.of())
                .build();
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
        statementDto = StatementDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .clientId(clientDto)
                .creditId(creditDto)
                .appliedOffer(loanOfferDto)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistoryList(List.of(
                        new StatusHistory(ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                ))
                .build();
    }

    @Test
    void getInfoFromDb() {
        statementService.setDealFeignClientUrl(dealFeignClientUrl);
        when(dealFeignClient.getStatementDto(anyString())).thenReturn(statementDto);

        StatementDto statementDtoFromService = statementService.getInfoFromDb(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"), anyString());

        assertNotNull(statementDtoFromService);
        assertEquals(statementDtoFromService, statementDto);

        verify(dealFeignClient, times(1)).getStatementDto(anyString());
    }

    @Test
    void getInfoFromDbFailFeign() {
        statementService.setDealFeignClientUrl(dealFeignClientUrl);
        FeignException feignException = FeignException.errorStatus("dealFeignClientUrl.getStatementDto",
                Response.builder()
                        .body("Test error message".getBytes())
                        .status(406)
                        .request(Request.create(Request.HttpMethod.POST, "ii", Map.of(), null, null, null))
                        .build());

        when(dealFeignClient.getStatementDto(anyString())).thenThrow(feignException);

        Exception e = assertThrows(ExternalServiceException.class, () ->
                statementService.getInfoFromDb(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"), anyString()));

        assertNotNull(e);
        assertArrayEquals("Test error message".getBytes(StandardCharsets.UTF_8),
                e.getMessage().getBytes(StandardCharsets.UTF_8));

        verify(dealFeignClient, times(1)).getStatementDto(anyString());
    }
}