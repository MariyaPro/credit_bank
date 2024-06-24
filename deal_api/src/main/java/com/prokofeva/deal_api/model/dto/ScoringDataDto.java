package com.prokofeva.deal_api.model.dto;

import com.prokofeva.deal_api.model.enums.Gender;
import com.prokofeva.deal_api.model.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Данные для детального расчета параметров кредита")
@Data
@Builder
public class ScoringDataDto {

    @Schema(description = "Сумма кредита", example = "300000")
    @NotNull
    @DecimalMin(value = "30000", message = "Сумма кредита должна быть не менее 30000.")
    private BigDecimal amount;

    @Schema(description = "Срок кредита", example = "36")
    @NotNull
    @Min(value = 6, message = "Минимальный срок кредита 6 месяцев")
    private Integer term;

    @Schema(description = "Имя",example = "Valentina")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String firstName;

    @Schema(description = "Фамилия",example = "Tereshkova")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String lastName;

    @Schema(description = "Отчество", example = "Vladimirovna")
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String middleName;

    @Schema(description = "Пол", example = "female")
    @NotNull
    private Gender gender;

    @Schema(description = "Дата рождения", example = "1987-03-06")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "5566")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}", message = "Серия паспорта состоит из 4х цифр.")
    private String passportSeries;

    @Schema(description = "Номер распорта", example = "456456")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}", message = "Номер паспорта состоит из 6ти цифр.")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта", example = "2000-04-20")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение, кем выдан паспорт", example = "ОВД г. Ярославля")
    @NotNull
    private String passportIssueBranch;

    @Schema(description = "Семейное положение",example = "married")
    @NotNull
    private MaritalStatus maritalStatus;

    @Schema(description = "Количество иждивенцев", example = "1")
    @NotNull
    private Integer dependentAmount;

    @NotNull
    private EmploymentDto employment;

    @Schema(description = "Номер аккаунта", example = "123e4567-e89b-12d3-a456-426655440000")
    @NotNull
    private String accountNumber;

    @Schema(description = "Включена ли страховка кредита", example = "true")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент",example = "false")
    @NotNull
    private Boolean isSalaryClient;

}

