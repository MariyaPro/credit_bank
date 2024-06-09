package com.prokofeva.deal_api.doman;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class Passport {

    @NotNull
    private String series;

    @NotNull
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}
