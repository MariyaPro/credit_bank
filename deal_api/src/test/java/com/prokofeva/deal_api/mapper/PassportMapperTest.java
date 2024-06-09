package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PassportMapperTest {

    @Autowired
    private PassportMapper passportMapper;

    private static Passport passportAc;
    private static PassportDto passportDtoAc;

    @BeforeAll
    static void preparePassportEx() {
        passportAc = new Passport();
        passportAc.setSeries("3434");
        passportAc.setNumber("123123");
        passportAc.setIssueBranch("Abc abc");
        passportAc.setIssueDate(LocalDate.of(2000, 3, 10));
    }

    @BeforeAll
    static void preparePassportDtoAc() {
        passportDtoAc = new PassportDto();
        passportDtoAc.setSeries("3434");
        passportDtoAc.setNumber("123123");
        passportDtoAc.setIssueBranch("Abc abc");
        passportDtoAc.setIssueDate(LocalDate.of(2000, 3, 10));
    }

    @Test
    void convertEntityToDto() {
        PassportDto passportDtoEx = passportMapper.convertEntityToDto(passportAc);

        assertNotNull(passportDtoEx);
        assertEquals(passportDtoEx, passportDtoAc);

    }

    @Test
    void convertDtoToEntity() {
        Passport passportEx = passportMapper.convertDtoToEntity(passportDtoAc);

        assertNotNull(passportEx);
        assertEquals(passportEx, passportAc);
    }
}