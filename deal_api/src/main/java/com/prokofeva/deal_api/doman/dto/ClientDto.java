package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.GenderEnum;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class ClientDto {
    @NotNull
    private UUID clientId;
    @NotNull
    private String lastName;
    @NotNull
    private String firstName;
    @NotNull
    private String middleName;
    @NotNull
    private LocalDate birthDate;
    @NotNull
    private String email;

    private GenderEnum gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;
    @NotNull
    private UUID passportId;

    private UUID employmentId;

    private String accountNumber;
}
