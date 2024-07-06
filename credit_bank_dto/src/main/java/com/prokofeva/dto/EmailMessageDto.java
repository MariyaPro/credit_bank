package com.prokofeva.dto;

import com.prokofeva.enums.Theme;
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