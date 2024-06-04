package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    private UUID statementId;
    private UUID clientId;
    private UUID creditId;
    private ApplicationStatus status;
    private Timestamp creationDate;
    private String appliedOffer;        //todo jsonb
    private Timestamp singDate;
    private String sesCode;     //todo varchar?
}
