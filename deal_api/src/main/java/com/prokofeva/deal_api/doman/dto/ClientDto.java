package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.Employment;
import com.prokofeva.deal_api.doman.Passport;
import com.prokofeva.deal_api.doman.enums.GenderEnum;
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
    private GenderEnum gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private Passport passportId;
    private Employment employmentId;
    private String accountNumber;
}
