package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.model.Client;
import com.prokofeva.deal_api.model.Statement;
import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.repositories.StatementRepo;
import com.prokofeva.dto.*;
import com.prokofeva.enums.ApplicationStatus;
import com.prokofeva.enums.ChangeType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static com.github.dockerjava.api.model.SELContext.none;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class StatementServiceImplTest {
    @Autowired
    private StatementServiceImpl statementService;
    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private StatementRepo statementRepo;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14-alpine");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> none);
    }

    @BeforeEach
    void setUp() {
        statementRepo.deleteAll();
    }

    @Test
    void createStatement() {
        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();

        ClientDto clientDtoAc = ClientDto.builder()
                .clientId(UUID.fromString("da2a5da5-19b7-475e-b8cb-f7eddec4f6b1"))
                .lastName("LastNam_clientAc")
                .firstName("FirstName_clientAc")
                .middleName("MiddleName_clientAc")
                .birthDate(LocalDate.of(2000, 2, 21))
                .email("mail.example_clientAc@gmail.com")
                .passport(passport)
                .build();

        long countStatementBefore = statementRepo.count();

        StatementDto statementDtoEx = statementService.createStatement(clientDtoAc, "logId");

        long countStatementAfter = statementRepo.count();

        assertNotNull(statementDtoEx);
        assertNotNull(statementDtoEx.getClientId());
        assertNotNull(statementDtoEx.getCreationDate());
        assertNotNull(statementDtoEx.getStatusHistoryList());
        assertNotNull(statementDtoEx.getStatus());

        assertEquals(countStatementBefore, countStatementAfter - 1L);
        assertEquals(statementDtoEx.getStatusHistoryList().size(), 1);
        assertEquals(statementDtoEx.getStatus(), ApplicationStatus.PREAPPROVAL);

        assertNull(statementDtoEx.getCreditId());
        assertNull(statementDtoEx.getAppliedOffer());
        assertNull(statementDtoEx.getSignDate());
        assertNull(statementDtoEx.getSesCode());
    }

    @Test
    void selectAppliedOffer() {
        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();

        Client clientAc = new Client();
        clientAc.setLastName("LastNam_clientAc");
        clientAc.setFirstName("FirstName_clientAc");
        clientAc.setMiddleName("MiddleName_clientAc");
        clientAc.setBirthDate(LocalDate.of(2000, 2, 21));
        clientAc.setEmail("mail.example_clientAc@gmail.com");
        clientAc.setPassport(passport);

        Statement statementAc = new Statement();
        statementAc.setClientId(clientRepo.save(clientAc));
        statementAc.setStatus(ApplicationStatus.PREAPPROVAL);
        statementAc.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statementAc.setStatusHistoryList(List.of(
                        new StatusHistory(
                                ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                )
        );

        UUID uuid = statementRepo.saveAndFlush(statementAc).getStatementId();

        LoanOfferDto loanOfferDto = LoanOfferDto.builder()
                .statementId(uuid)
                .requestedAmount(BigDecimal.valueOf(100000))
                .totalAmount(BigDecimal.valueOf(111185))
                .term(12)
                .monthlyPayment(BigDecimal.valueOf(9263.45))
                .rate(BigDecimal.valueOf(20.00))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        long countStatementBefore = statementRepo.count();

        statementService.selectAppliedOffer(loanOfferDto);

        long countStatementAfter = statementRepo.count();

        Statement statementEx = statementRepo.findById(uuid).orElseThrow();

        assertEquals(countStatementBefore, countStatementAfter);
        assertNotNull(statementEx.getAppliedOffer());

    }

    @Test
    void getStatementById() {
        PassportDto passport = PassportDto.builder()
                .series("4321")
                .number("654321")
                .build();

        Client clientAc = new Client();
        clientAc.setLastName("LastNam_clientAc");
        clientAc.setFirstName("FirstName_clientAc");
        clientAc.setMiddleName("MiddleName_clientAc");
        clientAc.setBirthDate(LocalDate.of(2000, 2, 21));
        clientAc.setEmail("mail.example_clientAc@gmail.com");
        clientAc.setPassport(passport);

        Statement statementAc = new Statement();
        statementAc.setClientId(clientRepo.saveAndFlush(clientAc));
        statementAc.setStatus(ApplicationStatus.PREAPPROVAL);
        statementAc.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        statementAc.setStatusHistoryList(List.of(
                        new StatusHistory(
                                ApplicationStatus.PREAPPROVAL,
                                LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
                                ChangeType.AUTOMATIC)
                )
        );

        String uuidStr = statementRepo.saveAndFlush(statementAc).getStatementId().toString();

        long countStatementBefore = statementRepo.count();

        StatementDto statementDtoEx = statementService.getStatementById(uuidStr);

        long countStatementAfter = statementRepo.count();

        assertEquals(countStatementBefore, countStatementAfter);
        assertNotNull(statementDtoEx);

        assertEquals(statementDtoEx.getClientId().getClientId(), statementAc.getClientId().getClientId());
        assertEquals(statementDtoEx.getStatementId(), statementAc.getStatementId());
        assertEquals(statementDtoEx.getStatus(), statementAc.getStatus());
        assertEquals(statementDtoEx.getCreationDate(), statementAc.getCreationDate());
        assertEquals(statementDtoEx.getAppliedOffer(), statementAc.getAppliedOffer());
        assertEquals(statementDtoEx.getStatusHistoryList(), statementAc.getStatusHistoryList());

    }

    @Test
    void registrationCredit() {
    }
}