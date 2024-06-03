package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.Gender;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

public class Client {
    private UUID clientId;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private Passport passportId;
    private Employment employmentId;
    private String accountNumber;
}
