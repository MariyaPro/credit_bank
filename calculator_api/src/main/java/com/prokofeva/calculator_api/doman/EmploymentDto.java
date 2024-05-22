package com.prokofeva.calculator_api.doman;

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

    public enum EmploymentStatusEnum {
        UNEMPLOYED,
        SELF_EMPLOYED,
        BUSINESS_OWNER
    }

    public enum PositionEnum {
        MANAGER,
        TOP_MANAGER
    }
}

