package com.prokofeva.deal_api.mapper;

import com.prokofeva.deal_api.doman.Employment;
import com.prokofeva.deal_api.doman.dto.EmploymentDto;
import com.prokofeva.deal_api.doman.enums.EmploymentPosition;
import com.prokofeva.deal_api.doman.enums.EmploymentStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmploymentMapperTest {
    @Autowired
    private EmploymentMapper employmentMapper;

    private static Employment employmentAc;
    private static EmploymentDto employmentDtoAc;

    @BeforeAll
    static void prepareEmploymentEx() {
        employmentAc = new Employment();
        employmentAc.setStatus(EmploymentStatus.EMPLOYED);
        employmentAc.setEmployerInn("7743013902");
        employmentAc.setSalary(BigDecimal.valueOf(80000));
        employmentAc.setPosition(EmploymentPosition.MID_MANAGER);
        employmentAc.setWorkExperienceTotal(360);
        employmentAc.setWorkExperienceCurrent(18);
    }

    @BeforeAll
    static void prepareEmploymentDtoAc() {
        employmentDtoAc = new EmploymentDto();
        employmentDtoAc.setStatus(EmploymentStatus.EMPLOYED);
        employmentDtoAc.setEmployerInn("7743013902");
        employmentDtoAc.setSalary(BigDecimal.valueOf(80000));
        employmentDtoAc.setPosition(EmploymentPosition.MID_MANAGER);
        employmentDtoAc.setWorkExperienceTotal(360);
        employmentDtoAc.setWorkExperienceCurrent(18);
    }

    @Test
    void convertEntityToDto() {
        EmploymentDto employmentDtoEx = employmentMapper.convertEntityToDto(employmentAc);

        assertNotNull(employmentDtoEx);
        assertEquals(employmentDtoEx, employmentDtoAc);

    }

    @Test
    void convertDtoToEntity() {
        Employment employmentEx = employmentMapper.convertDtoToEntity(employmentDtoAc);

        assertNotNull(employmentEx);
        assertEquals(employmentEx, employmentAc);
    }
}
