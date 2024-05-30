package com.prokofeva.calculator_api.doman.dto;

import com.prokofeva.calculator_api.doman.enums.EmploymentStatusEnum;
import com.prokofeva.calculator_api.doman.enums.PositionEnum;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
public class EmploymentDto {
    @NotNull
    private EmploymentStatusEnum employmentStatus;

    @NotNull
    @Pattern(regexp = "^[0-9]*", message = "ИНН содержит только цифры.")
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

