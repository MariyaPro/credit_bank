package com.prokofeva.dto;

import com.prokofeva.enums.Theme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    private String address;

    private Theme theme;
    private UUID statementId;
}