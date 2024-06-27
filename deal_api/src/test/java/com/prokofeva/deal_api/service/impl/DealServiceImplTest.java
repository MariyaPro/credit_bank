package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.client.CalcFeignClient;
import com.prokofeva.deal_api.dto.*;
import com.prokofeva.deal_api.enums.*;
import com.prokofeva.deal_api.exeption.ExternalServiceException;
import com.prokofeva.deal_api.service.ClientService;
import com.prokofeva.deal_api.service.CreditService;
import com.prokofeva.deal_api.service.StatementService;
import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {
    @InjectMocks
    private DealServiceImpl dealService;
    @Mock
    private ClientService clientService;
    @Mock
    private StatementService statementService;
    @Mock
    private CalcFeignClient calcFeignClient;
    @Mock
    private CreditService creditService;

    @Test
    void getListOffers() {
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

        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();
        ClientDto clientDtoFromService = ClientDto.builder()
                .clientId(UUID.fromString("4b853181-7290-4a7d-bd28-7ddb2706287d"))
                .lastName("LastName")
                .firstName("FirstName")
                .middleName("MiddleName")
                .birthDate(LocalDate.of(2000, 2, 21))
                .email("mail.example@gmail.com")
                .passport(passport)
                .build();

        StatementDto statementDtoFromService = StatementDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .clientId(clientDtoFromService)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistoryList(List.of(
                        new StatusHistory(ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                ))
                .build();

        when(clientService.createClient(any(), anyString())).thenReturn(ClientDto.builder().build());
        when(statementService.createStatement(any(), anyString())).thenReturn(statementDtoFromService);
        when(calcFeignClient.getListOffers(any(LoanStatementRequestDto.class))).thenReturn(List.of(
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build(),
                LoanOfferDto.builder().build()
        ));

        List<LoanOfferDto> listOffersAc = dealService.getListOffers(loanRequestDto);

        assertNotNull(listOffersAc);

        assertEquals(4, listOffersAc.size());
        assertEquals(statementDtoFromService.getStatementId(), listOffersAc.get(0).getStatementId());
        assertEquals(statementDtoFromService.getStatementId(), listOffersAc.get(1).getStatementId());
        assertEquals(statementDtoFromService.getStatementId(), listOffersAc.get(2).getStatementId());
        assertEquals(statementDtoFromService.getStatementId(), listOffersAc.get(3).getStatementId());

        verify(clientService, times(1)).createClient(any(LoanStatementRequestDto.class), anyString());
        verify(statementService, times(1)).createStatement(any(ClientDto.class), anyString());
        verify(calcFeignClient, times(1)).getListOffers(any(LoanStatementRequestDto.class));
    }

    @Test
    void getListOffersFail() {
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
        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();
        ClientDto clientDtoFromService = ClientDto.builder()
                .clientId(UUID.fromString("4b853181-7290-4a7d-bd28-7ddb2706287d"))
                .lastName("LastName")
                .firstName("FirstName")
                .middleName("MiddleName")
                .birthDate(LocalDate.of(2000, 2, 21))
                .email("mail.example@gmail.com")
                .passport(passport)
                .build();
        StatementDto statementDtoFromService = StatementDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .clientId(clientDtoFromService)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistoryList(List.of(
                        new StatusHistory(ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                ))
                .build();
        FeignException feignException = FeignException.errorStatus("calcFeignClient.getListOffers",
                Response.builder()
                        .body("Test message".getBytes())
                        .status(406)
                        .request(Request.create(Request.HttpMethod.POST, "ii", Map.of(), null, null, null))
                        .build());

        when(clientService.createClient(any(LoanStatementRequestDto.class), anyString())).thenReturn(ClientDto.builder().build());
        when(statementService.createStatement(any(), anyString())).thenReturn(statementDtoFromService);
        when(calcFeignClient.getListOffers(any(LoanStatementRequestDto.class))).thenThrow(feignException);

        Exception e = assertThrows(ExternalServiceException.class, () -> dealService.getListOffers(loanRequestDto));

        assertNotNull(e);
        assertArrayEquals("Test message".getBytes(StandardCharsets.UTF_8),
                e.getMessage().getBytes(StandardCharsets.UTF_8));

        verify(clientService, times(1)).createClient(any(LoanStatementRequestDto.class), anyString());
        verify(statementService, times(1)).createStatement(any(ClientDto.class), anyString());
        verify(calcFeignClient, times(1)).getListOffers(any(LoanStatementRequestDto.class));

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

        dealService.selectAppliedOffer(loanOfferDto);

        verify(statementService, times(1)).selectAppliedOffer(loanOfferDto);
    }

    @Test
    void registrationCredit() {
        EmploymentDto employmentDto = EmploymentDto.builder()
                .status(EmploymentStatus.EMPLOYED)
                .employerInn("505021556592")
                .salary(BigDecimal.valueOf(260000))
                .position(EmploymentPosition.TOP_MANAGER)
                .workExperienceTotal(300)
                .workExperienceCurrent(10)
                .build();
        FinishRegistrationRequestDto finRegRequestDto = FinishRegistrationRequestDto.builder()
                .employment(employmentDto)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .passportIssueDate(LocalDate.of(2010, 2, 21))
                .passportIssueBrach("В уездном городе N")
                .accountNumber("5610000123")
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
        StatementDto statementDto = StatementDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .clientId(clientDto)
                .appliedOffer(loanOfferDto)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistoryList(List.of(
                        new StatusHistory(ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                ))
                .build();


        when(statementService.getStatementById("fceaf46f-08f4-462f-9267-cc03047835a5")).thenReturn(statementDto);
        when(clientService.updateClientInfo(statementDto.getClientId(), finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5")).thenReturn(clientDto);
        when(calcFeignClient.calculateCredit(any())).thenReturn(creditDto);
        when(creditService.createCredit(any(CreditDto.class), anyString())).thenReturn(creditDto);

        dealService.registrationCredit(finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5");

        verify(statementService, times(1)).getStatementById(any());
        verify(clientService, times(1)).updateClientInfo(any(), any(), anyString());
        verify(calcFeignClient, times(1)).calculateCredit(any());
        verify(creditService, times(1)).createCredit(any(), anyString());

    }

    @Test
    void registrationCreditFail() {
        EmploymentDto employmentDto = EmploymentDto.builder()
                .status(EmploymentStatus.EMPLOYED)
                .employerInn("505021556592")
                .salary(BigDecimal.valueOf(260000))
                .position(EmploymentPosition.TOP_MANAGER)
                .workExperienceTotal(300)
                .workExperienceCurrent(10)
                .build();
        FinishRegistrationRequestDto finRegRequestDto = FinishRegistrationRequestDto.builder()
                .employment(employmentDto)
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(2)
                .passportIssueDate(LocalDate.of(2010, 2, 21))
                .passportIssueBrach("В уездном городе N")
                .accountNumber("5610000123")
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
        StatementDto statementDto = StatementDto.builder()
                .statementId(UUID.fromString("fceaf46f-08f4-462f-9267-cc03047835a5"))
                .clientId(clientDto)
                .appliedOffer(loanOfferDto)
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .statusHistoryList(List.of(
                        new StatusHistory(ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                ))
                .build();

        FeignException feignException = FeignException.errorStatus("calcFeignClient.getListOffers",
                Response.builder()
                        .body("Test message".getBytes())
                        .status(406)
                        .request(Request.create(Request.HttpMethod.POST, "ii", Map.of(), null, null, null))
                        .build());

        when(statementService.getStatementById("fceaf46f-08f4-462f-9267-cc03047835a5")).thenReturn(statementDto);
        when(clientService.updateClientInfo(statementDto.getClientId(), finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5")).thenReturn(clientDto);
        when(calcFeignClient.calculateCredit(any())).thenThrow(feignException);

        Exception e = assertThrows(ExternalServiceException.class,
                () -> dealService.registrationCredit(finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5"));

        assertNotNull(e);
        assertArrayEquals("Test message".getBytes(StandardCharsets.UTF_8),
                e.getMessage().getBytes(StandardCharsets.UTF_8));

        verify(statementService, times(1)).getStatementById(any());
        verify(clientService, times(1)).updateClientInfo(any(), any(), anyString());
        verify(calcFeignClient, times(1)).calculateCredit(any());
        verify(creditService, times(0)).createCredit(any(), anyString());
    }
}