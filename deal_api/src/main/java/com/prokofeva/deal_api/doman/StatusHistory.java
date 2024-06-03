package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.ChangeType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@AllArgsConstructor
@Data
public class StatusHistory {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
