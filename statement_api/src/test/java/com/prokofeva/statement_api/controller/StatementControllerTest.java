package com.prokofeva.statement_api.controller;

import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.statement_api.service.impl.StatementServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StatementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private StatementController statementController;

    @Mock
    private StatementServiceImpl statementService;

    @Test
    void createLoanOffer() {
        LoanStatementRequestDto requestDto = LoanStatementRequestDto.builder()
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
        when(statementService.createListOffer(any(LoanStatementRequestDto.class), any()))
                .thenReturn(List.of(
                        LoanOfferDto.builder().build(), LoanOfferDto.builder().build(),
                        LoanOfferDto.builder().build(), LoanOfferDto.builder().build()
                ));

        ResponseEntity<List<LoanOfferDto>> response = statementController.createLoanOffer(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(4, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void createLoanOffer_NotValid() throws Exception {
        // запрашиваемая сумма и срок  меньше минимума
        MockHttpServletRequestBuilder requestBuilder = post("/statement/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                         "amount": 20,
                         "term": 4,
                         "firstName": "string",
                         "lastName": "string",
                         "middleName": "string",
                         "email": "string@gmail.com",
                         "birthdate": "2000-08-24",
                         "passportSeries": "1234",
                         "passportNumber": "123456"
                        }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().is4xxClientError(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{
                                        "fieldName": "amount",
                                        "message": "Сумма кредита должна быть не менее 30000."
                                    },
                                    {   "fieldName": "term",
                                         "message": "Минимальный срок кредита 6 месяцев"
                                    }]
                                """)
                );

        // валидация имени
        requestBuilder = post("/statement/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "amount": 30000,
                        "term": 6,
                        "firstName": "string!",
                        "lastName": "s",
                                              
                        "email": "string@gmail.com",
                        "birthdate": "2000-08-24",
                        "passportSeries": "1234",
                        "passportNumber": "123456"
                        }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{
                                             "fieldName": "lastName",
                                             "message": "От 2 до 30 символов."
                                         },
                                         {
                                             "fieldName": "firstName",
                                             "message": "Только латинские бувкы."
                                         }]
                                """)
                );

        // валидация данных паспорта и почты
        requestBuilder = post("/statement/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "amount": 30000,
                        "term": 6,
                        "firstName": "firstName",
                        "lastName": "lastName",
                        "middleName": "middleName",
                        "email": "string-gmail.com",
                        "birthdate": "2000-08-24",
                        "passportSeries": "123",
                        "passportNumber": "1234o6"
                        }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{
                                                  "fieldName": "passportSeries",
                                                  "message": "Серия паспорта состоит из 4х цифр."
                                              },
                                              {
                                                  "fieldName": "email",
                                                  "message": "Некорректный email."
                                              },
                                              {
                                                  "fieldName": "passportNumber",
                                                  "message": "Номер паспорта состоит из 6ти цифр."
                                              }]
                                """)
                );
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

        ResponseEntity<Void> response = statementController.selectAppliedOffer(loanOfferDto);

        verify(statementService, times(1)).selectAppliedOffer(loanOfferDto, "fceaf46f-08f4-462f-9267-cc03047835a5");
        assertEquals(response.getStatusCodeValue(), 200);
    }
}