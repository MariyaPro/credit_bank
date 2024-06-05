package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID statementId;

    @OneToOne(mappedBy = "statement_id")
    @JoinColumn(name = "client_id")
    private Client clientId;

    @OneToOne(mappedBy = "statement_id")
    @JoinColumn(name = "credit_id")
    private Credit creditId;

    @NotNull
    private ApplicationStatus status;

    @NotNull
    private Timestamp creationDate;

    @NotNull
    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    private Timestamp signDate;

    private String sesCode;     //todo varchar?
}
