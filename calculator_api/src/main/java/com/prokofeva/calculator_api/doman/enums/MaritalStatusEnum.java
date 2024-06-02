package com.prokofeva.calculator_api.doman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MaritalStatusEnum {
    MARRIED("married"),
    DIVORCED("divorced"),
    SINGLE("single");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MaritalStatusEnum fromValue(String value) {
        for (MaritalStatusEnum b : MaritalStatusEnum.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
