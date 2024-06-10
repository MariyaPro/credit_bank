package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.EmploymentPosition;
import com.prokofeva.deal_api.doman.enums.EmploymentStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class Employment {

    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @NotNull
    private String employerInn;

    @NotNull
    private BigDecimal salary;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;
}
