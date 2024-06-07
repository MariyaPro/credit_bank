package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StatementDto {
    private UUID statementId;
    private ClientDto clientId;
    private CreditDto creditId;
    private ApplicationStatus status;
    private LocalDateTime creationDate;
    private LoanOfferDto appliedOffer;
    private LocalDateTime signDate;
    private String sesCode;
}
