package com.prokofeva.deal_api.doman.dto;

import com.prokofeva.deal_api.doman.enums.ApplicationStatus;
import com.prokofeva.deal_api.doman.enums.ChangeType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@AllArgsConstructor
public class StatusHistory {
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}
