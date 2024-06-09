package com.prokofeva.deal_api.doman.dto;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.TypeDef;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class PassportDto {

    @Schema(description = "Серия паспорта", example = "5566")
    @NotNull
    @Pattern(regexp = "^[0-9]{4}", message = "Серия паспорта состоит из 4х цифр.")
    private String series;

    @Schema(description = "Номер распорта", example = "456456")
    @NotNull
    @Pattern(regexp = "^[0-9]{6}", message = "Номер паспорта состоит из 6ти цифр.")
    private String number;

    @Schema(description = "Отделение, кем выдан паспорт", example = "ОВД г. Ярославля")
    private String issueBranch;

    @Schema(description = "Дата выдачи паспорта", example = "2000-04-20")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate issueDate;
}
