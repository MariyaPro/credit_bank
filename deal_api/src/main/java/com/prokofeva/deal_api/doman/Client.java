package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.EmploymentDto;
import com.prokofeva.deal_api.doman.dto.PassportDto;
import com.prokofeva.deal_api.doman.enums.Gender;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
public class Client {
    @Id
    @GeneratedValue
    @Column(name = "client_id")
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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @Type(type = "jsonb")
    private PassportDto passport;

    @Type(type = "jsonb")
    private EmploymentDto employment;

    private String accountNumber;

}
