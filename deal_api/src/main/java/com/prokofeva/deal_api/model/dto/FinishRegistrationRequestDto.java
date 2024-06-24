package com.prokofeva.deal_api.model.dto;

import com.prokofeva.deal_api.model.enums.Gender;
import com.prokofeva.deal_api.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Данные для детального расчета параметров кредита")
@Data
@Builder
public class FinishRegistrationRequestDto {
    @Schema(description = "Пол", example ="male")
    @NotNull
    private Gender gender;

    @Schema(description = "СемеСемейное положение",example = "married")
    @NotNull
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    @NotNull
    private Integer dependentAmount;

    @Schema(description = "Дата выдачи паспорта", example = "2000-03-09")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение, кем выдан паспорт", example = "ОВД г. Ярославля")
    @NotNull
    private String passportIssueBrach;

    @NotNull
    private EmploymentDto employment;

    @Schema(description = "Номер аккаунта", example = "123e4567-e89b-12d3-a456-426655440000")
    @NotNull
    private String accountNumber;
}
