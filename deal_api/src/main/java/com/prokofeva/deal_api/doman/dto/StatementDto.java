package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class StatementDto {
    private UUID statementId;
    private UUID clientId;
    private UUID creditId;
    private ApplicationStatus status;
    private LocalDateTime creationDate;
    private LoanOfferDto appliedOffer;
    private LocalDateTime signDate;
    private String sesCode;     //todo varchar?
}
