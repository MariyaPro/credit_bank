package com.prokofeva.deal_api.dto;

import com.prokofeva.deal_api.enums.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Schema(description = "Сообщение на почту клиенту.")
@Data
@Builder
public class EmailMessageDto {

    private String address;
    private Theme theme;
    private UUID statementId;

}