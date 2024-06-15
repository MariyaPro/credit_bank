package com.prokofeva.deal_api.controller;

import com.prokofeva.deal_api.repositories.ClientRepo;
import com.prokofeva.deal_api.repositories.CreditRepo;
import com.prokofeva.deal_api.repositories.StatementRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@RequiredArgsConstructor
@AutoConfigureMockMvc
class DealControllerTest {

    private final MockMvc mockMvc;

    private final ClientRepo clientRepo;
    private final CreditRepo creditRepo;
    private final StatementRepo statementRepo;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:14.7-alpine"));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", ()->true);
    }

//    @BeforeAll
//    static void beforeAll() {
//        postgres.start();
//    }

//    @AfterAll
//    static void afterAll() {
//        postgres.stop();
//    }

    @BeforeEach
    void setUp() {
        clientRepo.deleteAll();
        creditRepo.deleteAll();
        statementRepo.deleteAll();
    }


    @SneakyThrows
    @Test
    void getLoanOffers() {
        MockHttpServletRequestBuilder requestBuilder = post("/deal/statement")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                          "amount": 300000,
                          "term": 6,
                          "firstName": "firstName",
                          "lastName": "lastName",
                          "middleName": "middleName",
                          "email": "string@gmail.com",
                          "birthdate": "2000-08-24",
                          "passportSeries": "1234",
                          "passportNumber": "123456"
                          }
                        """);

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void selectAppliedOffer() {

    }

    @Test
    void registrationCredit() {
    }
}