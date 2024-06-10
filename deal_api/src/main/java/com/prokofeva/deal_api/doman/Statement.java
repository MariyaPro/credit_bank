package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.dto.LoanOfferDto;
import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    @GeneratedValue
    private UUID statementId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client clientId;

    @OneToOne()
    @JoinColumn(name = "credit_id")
    private Credit creditId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @NotNull
    private LocalDateTime creationDate;

    @Type(type = "jsonb")
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    @Type(type = "jsonb")
    private List<StatusHistory> statusHistoryList;

    private String sesCode;
}
