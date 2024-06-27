package com.prokofeva.deal_api.dto;

import com.prokofeva.deal_api.enums.ApplicationStatus;
import com.prokofeva.deal_api.enums.ChangeType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@TypeDef(name = "json", typeClass = JsonType.class)
@AllArgsConstructor
public class StatusHistory {
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    private ChangeType changeType;
}
