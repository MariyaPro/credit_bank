package com.prokofeva.calculator_api.doman.dto;

import com.prokofeva.calculator_api.doman.enums.EmploymentStatusEnum;
import com.prokofeva.calculator_api.doman.enums.PositionEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class EmploymentDto {
    @NotNull
    private EmploymentStatusEnum employmentStatus;

    @NotNull
    private String employerINN;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private PositionEnum position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;

}

