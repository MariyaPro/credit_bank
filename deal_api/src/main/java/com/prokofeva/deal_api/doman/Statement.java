package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;

import java.sql.Timestamp;
import java.util.UUID;

public class Statement {
    private UUID statementId;
    private UUID clientId;
    private UUID creditId;
    private ApplicationStatus status;
    private Timestamp creationDate;
    private String appliedOffer;        //todo jsonb
    private Timestamp singDate;
    private String sesCode;     //todo varchar?
}
