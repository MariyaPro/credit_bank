package com.prokofeva.deal_api.model;

import com.prokofeva.deal_api.dto.LoanOfferDto;
import com.prokofeva.deal_api.dto.StatusHistory;
import com.prokofeva.deal_api.enums.ApplicationStatus;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@TypeDef(name = "json", typeClass = JsonType.class)
public class Statement {
    @Id
    @GeneratedValue
    @Type(type = "pg-uuid")
    private UUID statementId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @NotNull
    private Client clientId;

    @OneToOne
    @JoinColumn(name = "credit_id")
    private Credit creditId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ApplicationStatus status;

    @NotNull
    private LocalDateTime creationDate;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<StatusHistory> statusHistoryList;

    private String sesCode;
}
