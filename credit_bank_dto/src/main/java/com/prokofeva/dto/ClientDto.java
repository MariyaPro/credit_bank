package com.prokofeva.dto;

import com.prokofeva.enums.Gender;
import com.prokofeva.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ClientDto {

    private UUID clientId;

    @Schema(description = "Фамилия", example = "Gagarin")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String lastName;

    @Schema(description = "Имя",example = "Yuri")
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String firstName;

    @Schema(description = "Отчество", example = "Alekseevich")
    @Pattern(regexp = "^[a-zA-Z]*", message = "Только латинские бувкы.")
    @Size(min = 2, max = 30, message = "От 2 до 30 символов.")
    private String middleName;

    @Schema(description = "Дата рождения", example = "1984-03-09")
    @NotNull
    private LocalDate birthDate;

    @NotNull
    @Schema(description = "Адрес электронной почты", example = "Gagarin@mail.ru")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректный email.")
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private PassportDto passport;
    private EmploymentDto employment;
    private String accountNumber;
}