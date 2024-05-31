package com.prokofeva.calculator_api.doman.dto;

import com.prokofeva.calculator_api.doman.enums.EmploymentStatusEnum;
import com.prokofeva.calculator_api.doman.enums.PositionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Schema(description = "Информация о занятости")
@Data
public class EmploymentDto {
    @Schema(description = "Статус")
    @NotNull
    private EmploymentStatusEnum employmentStatus;

    @Schema(description = "личный ИНН")
    @NotNull
    @Pattern(regexp = "^[0-9]*", message = "ИНН содержит только цифры.")
    private String employerINN;

    @Schema(description = "Зарплата")
    @NotNull
    private BigDecimal salary;

    @Schema(description = "Должность")
    @NotNull
    private PositionEnum position;

    @Schema(description = "Общий трудовой  стаж")
    @NotNull
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы")
    @NotNull
    private Integer workExperienceCurrent;

}

