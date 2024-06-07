package com.prokofeva.calculator_api.doman.dto;

import com.prokofeva.calculator_api.doman.enums.EmploymentStatus;
import com.prokofeva.calculator_api.doman.enums.EmploymentPosition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Schema(description = "Информация о занятости")
@Data
public class EmploymentDto {
    @Schema(description = "Статус", example = "busy")
    @NotNull
    private EmploymentStatus status;

    @Schema(description = "личный ИНН", example = "505021556592")
    @NotNull
    @Pattern(regexp = "^[0-9]*", message = "ИНН содержит только цифры.")
    private String employerInn;

    @Schema(description = "Зарплата", example = "285000")
    @NotNull
    private BigDecimal salary;

    @Schema(description = "Должность", example = "top_manager")
    @NotNull
    private EmploymentPosition position;

    @Schema(description = "Общий трудовой  стаж", example = "360")
    @NotNull
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "96")
    @NotNull
    private Integer workExperienceCurrent;

}

