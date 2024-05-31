package com.prokofeva.calculator_api.doman.dto;

import com.prokofeva.calculator_api.doman.enums.GenderEnum;
import com.prokofeva.calculator_api.doman.enums.MaritalStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Данные для детального расчета параметров кредита")
@Data
public class ScoringDataDto {

    @Schema(description = "Сумма кредита")
    @NotNull
    @DecimalMin(value = "30000", message = "Сумма кредита должна быть не менее 30000.")
    private BigDecimal amount;

    @Schema(description = "Срок кредита")
    @NotNull
    @Min(value = 6, message = "Минимальный срок кредита 6 месяцев")
    private Integer term;

    @Schema(description = "Имя")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String firstName;

    @Schema(description = "Фамилия")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String lastName;

    @Schema(description = "Отчество")
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String middleName;

    @Schema(description = "Пол")
    @NotNull
    private GenderEnum gender;

    @Schema(description = "Дата рождения")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}", message = "Серия паспорта состоит из 4х цифр.")
    private String passportSeries;

    @Schema(description = "Номер распорта")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}", message = "Номер паспорта состоит из 6ти цифр.")
    private String passportNumber;

    @Schema(description = "Дата выдачи паспорта")
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate passportIssueDate;

    @Schema(description = "Отделение, кем выдан паспорт")
    @NotNull
    private String passportIssueBranch;

    @Schema(description = "Семейное положение")
    @NotNull
    private MaritalStatusEnum maritalStatus;

    @Schema(description = "Количество иждивенцев")
    @NotNull
    private Integer dependentAmount;

    @NotNull
    private EmploymentDto employment;

    @Schema(description = "Номер аккаунта")
    @NotNull
    private String accountNumber;

    @Schema(description = "Включена ли страховка кредита")
    @NotNull
    private Boolean isInsuranceEnabled;

    @Schema(description = "Зарплатный клиент")
    @NotNull
    private Boolean isSalaryClient;

}

