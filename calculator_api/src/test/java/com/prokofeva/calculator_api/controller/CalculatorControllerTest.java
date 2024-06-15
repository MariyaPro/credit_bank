package com.prokofeva.calculator_api.controller;

import com.prokofeva.calculator_api.CreatorValidDto;
import com.prokofeva.calculator_api.doman.dto.CreditDto;
import com.prokofeva.calculator_api.doman.dto.LoanOfferDto;
import com.prokofeva.calculator_api.doman.dto.LoanStatementRequestDto;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
import com.prokofeva.calculator_api.service.CalculatorService;
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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private CalculatorController controller;

    @Test
    void createLoanOffer() {
        LoanStatementRequestDto requestDto = CreatorValidDto.createLoanStatementRequestDto();
        when(calculatorService.createListOffer(requestDto))
                .thenReturn(List.of(
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto(),
                        CreatorValidDto.createLoanOfferDto()
                ));

        ResponseEntity<List<LoanOfferDto>> response = controller.createLoanOffer(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(4, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void createLoanOffer_Valid() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/calculator/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                          "amount": 300000,
                          "term": 6,
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
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    void createLoanOffer_NotValid() throws Exception {
        // запрашиваемая сумма и срок  меньше минимума
        MockHttpServletRequestBuilder requestBuilder = post("/calculator/offers")
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
                        status().isBadRequest(),
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
        requestBuilder = post("/calculator/offers")
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
        requestBuilder = post("/calculator/offers")
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
    void calculateCredit() {
        ScoringDataDto requestDto = CreatorValidDto.createScoringDataDto();

        when(calculatorService.calculateCredit(requestDto))
                .thenReturn(CreatorValidDto.createCreditDto());

        ResponseEntity<CreditDto> response = controller.calculateCredit(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(Objects.requireNonNull(response.getBody()).getTerm() + 1,
                Objects.requireNonNull(response.getBody()).getPaymentSchedule().size());
        verify(calculatorService, times(1)).calculateCredit(requestDto);
    }

    @Test
    void calculateCredit_NotValid() throws Exception {
        // запрашиваемая сумма и срок  меньше минимума
        MockHttpServletRequestBuilder requestBuilder = post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                         "amount": 500,
                         "term": 1,
                         "firstName": "strinuug",
                         "lastName": "string",
                         "middleName": "string",
                         "gender": "mAle",
                         "birthdate": "2000-08-24",
                         "passportSeries": "2222",
                         "passportNumber": "123456",
                         "passportIssueDate": "2000-08-24",
                         "passportIssueBranch": "string",
                         "maritalStatus": "single",
                         "dependentAmount": 0,
                         "employment": {
                         "employmentStatus": "BUSY",
                         "employerINN": "1234322323",
                         "salary": 50000,
                         "position": "employee",
                         "workExperienceTotal": 100,
                         "workExperienceCurrent": 10
                         },
                         "accountNumber": "1234432",
                         "isInsuranceEnabled": false,
                         "isSalaryClient": false
                         }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
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
        requestBuilder = post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                         "amount": 50000,
                         "term": 18,
                         "firstName": "strinuug!",
                         "lastName": "s",
                         "middleName": "stringщ",
                         "gender": "mAle",
                         "birthdate": "2000-08-24",
                         "passportSeries": "2222",
                         "passportNumber": "123456",
                         "passportIssueDate": "2000-08-24",
                         "passportIssueBranch": "string",
                         "maritalStatus": "single",
                         "dependentAmount": 0,
                         "employment": {
                         "employmentStatus": "BUSY",
                         "employerINN": "1234322323",
                         "salary": 50000,
                         "position": "employee",
                         "workExperienceTotal": 100,
                         "workExperienceCurrent": 10
                         },
                         "accountNumber": "1234432",
                         "isInsuranceEnabled": false,
                         "isSalaryClient": false
                         }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                               {
                                                   "fieldName": "firstName",
                                                   "message": "Только латинские бувкы."
                                               },
                                               {
                                                   "fieldName": "lastName",
                                                   "message": "От 2 до 30 символов."
                                               },
                                               {
                                                   "fieldName": "middleName",
                                                   "message": "Только латинские бувкы."
                                               }
                                           ]
                                """)
                );

        // валидация employment
        requestBuilder = post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                         {
                          "amount": 300000,
                          "term": 44,
                          "firstName": "strinuug",
                          "lastName": "sstr",
                          "middleName": "string",
                          "gender": "mAle",
                          "birthdate": "2000-08-24",
                          "passportSeries": "2222",
                          "passportNumber": "123456",
                          "passportIssueDate": "2000-08-24",
                          "passportIssueBranch": "string",
                          "maritalStatus": "single",
                          "dependentAmount": 0,
                          "employment": null,
                          "accountNumber": "1234432",
                          "isInsuranceEnabled": false,
                          "isSalaryClient": false
                          }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{
                                        "fieldName": "employment",
                                        "message": "must not be null"
                                    }]
                                """)
                );

        // валидация данных паспорта и имени
        requestBuilder = post("/calculator/calc")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "amount": 50000,
                           "term": 18,
                           "firstName": "strinuug",
                           "lastName": "sTr",
                           "middleName": "string",
                           "gender": "mAle",
                           "birthdate": "2000-08-24",
                           "passportSeries": "222",
                           "passportNumber": "123o56",
                           "passportIssueDate": "2000-08-24",
                           "passportIssueBranch": null,
                           "maritalStatus": "single",
                           "dependentAmount": 0,
                           "employment": {
                           "employmentStatus": "BUSY",
                           "employerINN": "1234322323",
                           "salary": 50000,
                           "position": "employee",
                           "workExperienceTotal": 100,
                           "workExperienceCurrent": 10
                           },
                           "accountNumber": "1234432",
                           "isInsuranceEnabled": false,
                           "isSalaryClient": false
                           }
                        """);

        this.mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [{
                                     "fieldName": "passportIssueBranch",
                                     "message": "must not be null"
                                 },
                                 {
                                     "fieldName": "passportSeries",
                                     "message": "Серия паспорта состоит из 4х цифр."
                                 },
                                 {
                                     "fieldName": "passportNumber",
                                     "message": "Номер паспорта состоит из 6ти цифр."
                                }]
                                """)
                );
    }
}