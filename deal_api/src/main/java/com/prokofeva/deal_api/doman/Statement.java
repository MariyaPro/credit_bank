package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    @GeneratedValue
    private UUID statementId;

    @ManyToOne()
    @JoinColumn(name = "client_id")
    @NotNull
    private Client clientId;

    @OneToOne()
    @JoinColumn(name = "credit_id")
    private Credit creditId;

    @NotNull
    private ApplicationStatus status;

    @NotNull
    private LocalDateTime creationDate;

    @NotNull
    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;
}
