package com.prokofeva.dto;

import com.prokofeva.enums.Theme;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Schema(description = "Сообщение о необходимости отправки уведомления.")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailMessageDto {
    @NotNull
    @NotEmpty
    @Schema(description = "Email клиента")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Некорректный email.")
    private String address;

    @NotNull (message = "Не заполнена тема уведомления.")
    @Schema(description = "Тема уведомления")
    private Theme theme;

    @NotNull (message = "Не корректный идентификатор заявки.")
    @Schema(description = "Id заявки")
    private UUID statementId;
}