package com.prokofeva.deal_api.doman;

import java.util.Date;
import java.util.UUID;

public class Passport {
    private UUID passportId;
    private final String series;
    private final String number;
    private String issueBranch;
    private Date issueDate;

    public Passport(String series, String number) {
        this.series = series;
        this.number = number;
    }
}
