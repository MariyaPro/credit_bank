package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Data
public class StatementDto {
    private UUID statementId;
    private UUID clientId;
    private UUID creditId;
    private ApplicationStatus status;
    private Timestamp creationDate;
    private String appliedOffer;        //todo jsonb
    private Timestamp signDate;
    private String sesCode;     //todo varchar?
}
