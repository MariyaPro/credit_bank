package com.prokofeva.calculator_api.doman;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@RequiredArgsConstructor
@Data
public class LoanStatementRequestDto {
    @NotNull
    @DecimalMin(value = "30000", message = "Сумма кредита должна быть не менее 30000.")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Минимальный срок кредита 6 месяцев")
    private Integer term;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]" , message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z]", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String middleName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректный email.")
    private String email;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^[0-9]", message = "Серия паспорта ссостоит из 4х цифр.")
    @Size(min = 4, max = 4)
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^[0-9]", message = "Номер паспорта состоит из 6ти цифр.")
    @Size(min = 6, max = 6)
    private String passportNumber;

}

