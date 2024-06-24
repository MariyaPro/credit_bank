package com.prokofeva.deal_api.service.impl;

import com.prokofeva.deal_api.model.dto.CreditDto;
import com.prokofeva.deal_api.model.enums.CreditStatus;
import com.prokofeva.deal_api.repositories.CreditRepo;
import com.prokofeva.deal_api.service.CreditService;
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
import java.util.List;

import static com.github.dockerjava.api.model.SELContext.none;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
class CreditServiceImplTest {
    @Autowired
    private CreditRepo creditRepo;

    @Autowired
    private CreditService creditService;

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
        creditRepo.deleteAll();
    }

    @Test
    void createCredit() {
        CreditDto creditDtoAc = CreditDto.builder()
                .amount(BigDecimal.valueOf(50000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(8826.14))
                .rate(BigDecimal.valueOf(20.00))
                .psk(BigDecimal.valueOf(19.974))
                .insuranceEnabled(false)
                .salaryClient(false)
                .paymentSchedule(List.of())
                .build();

        long countCreditBefore = creditRepo.count();

        CreditDto creditDtoEx = creditService.createCredit(creditDtoAc, "logId");

        long countCreditAfter = creditRepo.count();

        assertNotNull(creditDtoEx);
        assertNotNull(creditDtoEx.getCreditId());
        assertNotNull(creditDtoEx.getCreditStatus());

        assertEquals(countCreditBefore, countCreditAfter - 1L);

        assertEquals(creditDtoEx.getAmount(), creditDtoAc.getAmount());
        assertEquals(creditDtoEx.getTerm(), creditDtoAc.getTerm());
        assertEquals(creditDtoEx.getMonthlyPayment(), creditDtoAc.getMonthlyPayment());
        assertEquals(creditDtoEx.getRate(), creditDtoAc.getRate());
        assertEquals(creditDtoEx.getPsk(), creditDtoAc.getPsk());
        assertEquals(creditDtoEx.getInsuranceEnabled(), creditDtoAc.getInsuranceEnabled());
        assertEquals(creditDtoEx.getSalaryClient(), creditDtoAc.getSalaryClient());
        assertEquals(creditDtoEx.getPaymentSchedule(), creditDtoAc.getPaymentSchedule());
        assertEquals(creditDtoEx.getCreditStatus(), CreditStatus.CALCULATED);
    }
}