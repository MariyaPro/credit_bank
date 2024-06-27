package com.prokofeva.deal_api.dto;

import com.prokofeva.deal_api.enums.EmploymentPosition;
import com.prokofeva.deal_api.enums.EmploymentStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@TypeDef(name = "json", typeClass = JsonType.class)
@Schema(description = "Информация о занятости")
@Data
@Builder
public class EmploymentDto {
    @Schema(description = "Статус", example = "employed")
    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @Schema(description = "личный ИНН", example = "505021556592")
    @NotNull
    @Pattern(regexp = "^[0-9]*", message = "ИНН содержит только цифры.")
    private String employerInn;

    @Schema(description = "Зарплата", example = "285000")
    @NotNull
    private BigDecimal salary;

    @Schema(description = "Должность", example = "top manager")
    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;

    @Schema(description = "Общий трудовой  стаж", example = "360")
    @NotNull
    private Integer workExperienceTotal;

    @Schema(description = "Стаж на текущем месте работы", example = "96")
    @NotNull
    private Integer workExperienceCurrent;
}
