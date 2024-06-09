package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.Gender;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ClientDto {

    private UUID clientId;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private PassportDto passport;
    private EmploymentDto employment;
    private String accountNumber;
}
