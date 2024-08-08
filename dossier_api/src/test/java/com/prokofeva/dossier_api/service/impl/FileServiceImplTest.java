package com.prokofeva.dossier_api.service.impl;

import com.prokofeva.dto.*;
import com.prokofeva.enums.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@ExtendWith(SpringExtension.class)
class FileServiceImplTest {

    private final FileServiceImpl fileService = new FileServiceImpl();
    private static StatementDto statementDto ;

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
    void createCreditAgreementFile() throws IOException {
        Path creditAgreementFile = fileService.createCreditAgreementFile(statementDto, "test:fceaf46f-08f4-462f-9267-cc03047835a5");

        assertNotNull(creditAgreementFile);
        assertTrue(Files.size(creditAgreementFile) > 0L);
        assertTrue(creditAgreementFile.getFileName().toString()
                .startsWith("Credit_agreement_" + statementDto.getStatementId().toString()));
    }

    @Test
    void createQuestionnaireFile() throws IOException {
        Path questionnaireFile = fileService.createQuestionnaireFile(statementDto, "test:fceaf46f-08f4-462f-9267-cc03047835a5");

        assertNotNull(questionnaireFile);
        assertTrue(Files.size(questionnaireFile) > 0L);
        assertTrue(questionnaireFile.getFileName().toString()
                .startsWith("Questionnaire_" + statementDto.getStatementId().toString()));
    }

    @Test
    void createPaymentScheduleFile() throws IOException {
        Path paymentScheduleFile = fileService.createPaymentScheduleFile(statementDto, "test:fceaf46f-08f4-462f-9267-cc03047835a5");

        assertNotNull(paymentScheduleFile);
        assertTrue(Files.size(paymentScheduleFile) > 0L);
        assertTrue(paymentScheduleFile.getFileName().toString()
                .startsWith("Payment_Schedule_" + statementDto.getStatementId().toString()));
    }
}