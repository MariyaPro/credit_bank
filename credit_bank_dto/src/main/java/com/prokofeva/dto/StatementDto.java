package com.prokofeva.dto;

import com.prokofeva.enums.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Заявка на кредит.")
@Data
@Builder
public class StatementDto {
    private UUID statementId;
    private ClientDto clientId;
    private CreditDto creditId;
    private ApplicationStatus status;
    private LocalDateTime creationDate;
    private LoanOfferDto appliedOffer;
    private LocalDateTime signDate;
    private List<StatusHistory> statusHistoryList;
    private String sesCode;
}