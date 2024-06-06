package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID statementId;

//    @OneToOne(mappedBy = "statement_id")
//    @JoinColumn(name = "client_id")
    @NotNull
    private UUID clientId;

//    @OneToOne(mappedBy = "statement_id")
//    @JoinColumn(name = "credit_id")
    private UUID creditId;

    @NotNull
    private ApplicationStatus status;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;     //todo varchar?
}
