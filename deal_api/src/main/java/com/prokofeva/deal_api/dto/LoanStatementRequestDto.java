package com.prokofeva.deal_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Заявка на расчет вариантов займа.")
@Data
@Builder
public class LoanStatementRequestDto {
    @Schema(description = "Сумма кредита", example = "100000")
    @NotNull
    @DecimalMin(value = "30000", message = "Сумма кредита должна быть не менее 30000.")
    private BigDecimal amount;

    @Schema(description = "Срок кредита", example = "12")
    @NotNull
    @Min(value = 6, message = "Минимальный срок кредита 6 месяцев")
    private Integer term;

    @Schema(description = "Имя", example = "Yuri")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String firstName;

    @Schema(description = "Фамилия", example = "Gagarin")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String lastName;

    @Schema(description = "Отчество", example = "Alekseevich")
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String middleName;

    @NotNull
    @Schema(description = "Адрес электронной почты", example = "Gagarin@mail.ru")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректный email.")
    private String email;

    @Schema(description = "Дата рождения", example = "1984-03-09")
    @NotNull
    private LocalDate birthdate;

    @Schema(description = "Серия паспорта", example = "3434")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}", message = "Серия паспорта состоит из 4х цифр.")
    private String passportSeries;

    @Schema(description = "Номер паспорта", example = "123123")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}", message = "Номер паспорта состоит из 6ти цифр.")
    private String passportNumber;

}