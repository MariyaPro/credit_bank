package com.prokofeva.deal_api.doman;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor(force = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID passportId;

    @NotNull
    private final String series;

    @NotNull
    private final String number;
    private String issueBranch;
    private Date issueDate;

    public Passport(String series, String number) {
        this.series = series;
        this.number = number;
    }
}
