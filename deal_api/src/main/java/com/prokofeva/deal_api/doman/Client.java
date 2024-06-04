package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.GenderEnum;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Client {
    @Id
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
