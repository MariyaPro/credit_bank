

package com.prokofeva.deal_api.model;

import com.prokofeva.deal_api.dto.EmploymentDto;
import com.prokofeva.deal_api.dto.PassportDto;
import com.prokofeva.deal_api.enums.Gender;
import com.prokofeva.deal_api.enums.MaritalStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
public class Client {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
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

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private PassportDto passport;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private EmploymentDto employment;

    private String accountNumber;

}
