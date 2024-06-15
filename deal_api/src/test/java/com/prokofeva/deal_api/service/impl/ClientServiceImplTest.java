package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.doman.Client;
import com.prokofeva.deal_api.doman.dto.*;
import com.prokofeva.deal_api.doman.enums.EmploymentPosition;
import com.prokofeva.deal_api.doman.enums.EmploymentStatus;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import com.prokofeva.deal_api.repositories.ClientRepo;
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
import java.util.UUID;

import static com.github.dockerjava.api.model.SELContext.none;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
class ClientServiceImplTest {
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ClientServiceImpl clientService;

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
        clientRepo.deleteAll();
    }

    @Test
    void createClient() {
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

        long countClientBefore = clientRepo.count();

        ClientDto clientDtoEx = clientService.createClient(loanRequestDto);

        long countClientAfter = clientRepo.count();

        assertNotNull(clientDtoEx);
        assertNotNull(clientDtoEx.getClientId());
        assertEquals(countClientBefore, countClientAfter - 1L);

        assertNull(clientDtoEx.getPassport().getIssueBranch());
        assertNull(clientDtoEx.getPassport().getIssueDate());
        assertNull(clientDtoEx.getDependentAmount());
        assertNull(clientDtoEx.getAccountNumber());
        assertNull(clientDtoEx.getEmployment());
        assertNull(clientDtoEx.getGender());
        assertNull(clientDtoEx.getMaritalStatus());

        assertEquals(clientDtoEx.getPassport().getNumber(), loanRequestDto.getPassportNumber());
        assertEquals(clientDtoEx.getPassport().getSeries(), loanRequestDto.getPassportSeries());
        assertEquals(clientDtoEx.getBirthDate(), loanRequestDto.getBirthdate());
        assertEquals(clientDtoEx.getEmail(), loanRequestDto.getEmail());
        assertEquals(clientDtoEx.getFirstName(), loanRequestDto.getFirstName());
        assertEquals(clientDtoEx.getLastName(), loanRequestDto.getLastName());
        assertEquals(clientDtoEx.getMiddleName(), loanRequestDto.getMiddleName());
    }

    @Test
    void updateClientInfo() {
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

        UUID uuid = clientRepo.saveAndFlush(clientAc).getClientId();

        ClientDto clientDtoAc = ClientDto.builder()
                .clientId(uuid)
                .lastName("LastNam_clientAc")
                .firstName("FirstName_clientAc")
                .middleName("MiddleName_clientAc")
                .birthDate(LocalDate.of(2000, 2, 21))
                .email("mail.example_clientAc@gmail.com")
                .passport(passport)
                .build();

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

        long countClientBefore = clientRepo.count();

        ClientDto clientDtoEx = clientService.updateClientInfo(clientDtoAc, finRegRequestDto);

        long countClientAfter = clientRepo.count();

        assertNotNull(clientDtoEx);
        assertEquals(countClientBefore, countClientAfter);

        assertEquals(clientDtoEx.getClientId(), clientDtoAc.getClientId());
        assertEquals(clientDtoEx.getLastName(), clientDtoAc.getLastName());
        assertEquals(clientDtoEx.getFirstName(), clientDtoAc.getFirstName());
        assertEquals(clientDtoEx.getMiddleName(), clientDtoAc.getMiddleName());
        assertEquals(clientDtoEx.getBirthDate(), clientDtoAc.getBirthDate());
        assertEquals(clientDtoEx.getEmail(), clientDtoAc.getEmail());
        assertEquals(clientDtoEx.getPassport().getSeries(), clientDtoAc.getPassport().getSeries());
        assertEquals(clientDtoEx.getPassport().getNumber(), clientDtoAc.getPassport().getNumber());

        assertEquals(clientDtoEx.getEmployment(), finRegRequestDto.getEmployment());
        assertEquals(clientDtoEx.getMaritalStatus(), finRegRequestDto.getMaritalStatus());
        assertEquals(clientDtoEx.getDependentAmount(), finRegRequestDto.getDependentAmount());
        assertEquals(clientDtoEx.getPassport().getIssueDate(), finRegRequestDto.getPassportIssueDate());
        assertEquals(clientDtoEx.getPassport().getIssueBranch(), finRegRequestDto.getPassportIssueBrach());
        assertEquals(clientDtoEx.getAccountNumber(), finRegRequestDto.getAccountNumber());
    }

}