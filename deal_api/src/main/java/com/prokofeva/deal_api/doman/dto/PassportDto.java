package com.prokofeva.deal_api.doman.dto;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PassportDto {
    private UUID passportId;
    private String series;
    private String number;
    private String issueBranch;
    private Date issueDate;
}
