package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.GenderEnum;
import com.prokofeva.deal_api.doman.enums.MaritalStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.id.GUIDGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@RequiredArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    //@Type(type = "jsonb")
    @OneToOne(mappedBy = "client")
    @JoinColumn(name = "passport_id")
    private Passport passportId;

    //@Type(type = "jsonb")
    @OneToOne(mappedBy = "client")
    @JoinColumn(name = "employment_id")
    private Employment employmentId;

    private String accountNumber;
}
