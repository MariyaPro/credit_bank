package com.prokofeva.deal_api.controller;

import com.prokofeva.deal_api.service.DealService;
import com.prokofeva.dto.EmploymentDto;
import com.prokofeva.dto.FinishRegistrationRequestDto;
import com.prokofeva.dto.LoanOfferDto;
import com.prokofeva.dto.LoanStatementRequestDto;
import com.prokofeva.enums.EmploymentPosition;
import com.prokofeva.enums.EmploymentStatus;
import com.prokofeva.enums.MaritalStatus;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DealControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DealController dealController;
    @Mock
    private DealService dealService;

    @Test
    void getLoanOffers() {
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
        when(dealService.getListOffers(requestDto,anyString()))
                .thenReturn(List.of(
                        LoanOfferDto.builder().build(), LoanOfferDto.builder().build(),
                        LoanOfferDto.builder().build(), LoanOfferDto.builder().build()
                ));

        ResponseEntity<List<LoanOfferDto>> response = dealController.getLoanOffers(requestDto);

        assertNotNull(response);
        assertTrue(response.hasBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(4, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void getLoanOffers_NotValid() throws Exception {
        // запрашиваемая сумма и срок  меньше минимума
        MockHttpServletRequestBuilder requestBuilder = post("/deal/statement")
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
        requestBuilder = post("/deal/statement")
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
        requestBuilder = post("/deal/statement")
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

        ResponseEntity<Void> response = dealController.selectAppliedOffer(loanOfferDto);

        verify(dealService, times(1)).selectAppliedOffer(loanOfferDto, anyString());
        assertEquals(response.getStatusCodeValue(), 200);
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

        ResponseEntity<Void> response = dealController.registrationCredit(finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5");

        verify(dealService, times(1)).registrationCredit(finRegRequestDto, "fceaf46f-08f4-462f-9267-cc03047835a5", anyString());
        assertEquals(response.getStatusCodeValue(), 200);
    }
}