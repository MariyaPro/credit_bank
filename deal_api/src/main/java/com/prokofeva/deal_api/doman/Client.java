package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.Gender;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue
    private UUID clientId;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    private String email;

    private Gender gender;

    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @Type(type = "jsonb")
    private Passport passport;

    @Type(type = "jsonb")
    private Employment employment;

    private String accountNumber;

}
