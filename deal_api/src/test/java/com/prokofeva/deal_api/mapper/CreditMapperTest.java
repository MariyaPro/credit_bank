package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.model.Credit;
import com.prokofeva.dto.CreditDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreditMapperTest {
    private final CreditMapper creditMapper = Mappers.getMapper(CreditMapper.class);
    private static Credit creditAc;
    private static CreditDto creditDtoAc;

    @BeforeAll
    static void prepareCreditAc() {
        creditAc = new Credit();

        creditAc.setAmount(BigDecimal.valueOf(50000));
        creditAc.setTerm(6);
        creditAc.setMonthlyPayment(BigDecimal.valueOf(8826.14));
        creditAc.setRate(BigDecimal.valueOf(20.00));
        creditAc.setPsk(BigDecimal.valueOf(19.974));
        creditAc.setInsuranceEnabled(false);
        creditAc.setSalaryClient(false);
        creditAc.setPaymentSchedule(new ArrayList<>());
    }

    @BeforeAll
    static void prepareCreditDtoAc() {
        creditDtoAc = CreditDto.builder()
                .amount(BigDecimal.valueOf(50000))
                .term(6)
                .monthlyPayment(BigDecimal.valueOf(8826.14))
                .rate(BigDecimal.valueOf(20.00))
                .psk(BigDecimal.valueOf(19.974))
                .insuranceEnabled(false)
                .salaryClient(false)
                .paymentSchedule(new ArrayList<>())
                .build();
    }

    @Test
    void convertEntityToDto() {
        CreditDto creditDtoEx = creditMapper.convertEntityToDto(creditAc);

        assertNotNull(creditDtoEx);
        assertEquals(creditDtoEx, creditDtoAc);
    }

    @Test
    void convertDtoToEntity() {
        Credit creditEx = creditMapper.convertDtoToEntity(creditDtoAc);

        assertNotNull(creditEx);
        assertEquals(creditEx, creditAc);
    }
}