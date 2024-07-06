package com.prokofeva.dossier_api.dto;

import com.prokofeva.dossier_api.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class EmailMessageDto {
    private String address;
    private Theme theme;
    private UUID statementId;
}
