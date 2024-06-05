package com.prokofeva.deal_api.doman;

import com.prokofeva.deal_api.doman.enums.ChangeType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.sql.Timestamp;

@Data
//@TypeDefs({
//        @TypeDef(name = "json", typeClass = JsonStringType.class),
//        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
//})
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class StatusHistory {
    private String status;
    private Timestamp time;
    private ChangeType changeType;
}
