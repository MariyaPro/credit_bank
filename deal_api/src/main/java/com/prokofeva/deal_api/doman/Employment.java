package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.EmploymentPosition;
import com.prokofeva.deal_api.doman.enums.EmploymentStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@RequiredArgsConstructor
//@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID employmentId;

    @NotNull
    private EmploymentStatus status;

    @NotNull
    private String employerInn;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private EmploymentPosition position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;
}
