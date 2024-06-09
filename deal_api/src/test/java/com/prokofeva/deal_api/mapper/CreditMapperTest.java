package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Credit;
import com.prokofeva.deal_api.doman.dto.CreditDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CreditMapperTest {
    @Autowired
    private CreditMapper creditMapper;
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
        creditDtoAc = new CreditDto();

        creditDtoAc.setAmount(BigDecimal.valueOf(50000));
        creditDtoAc.setTerm(6);
        creditDtoAc.setMonthlyPayment(BigDecimal.valueOf(8826.14));
        creditDtoAc.setRate(BigDecimal.valueOf(20.00));
        creditDtoAc.setPsk(BigDecimal.valueOf(19.974));
        creditDtoAc.setInsuranceEnabled(false);
        creditDtoAc.setSalaryClient(false);
        creditDtoAc.setPaymentSchedule(new ArrayList<>());
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