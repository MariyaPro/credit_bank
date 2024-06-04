package com.prokofeva.calculator_api.doman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PositionEnum {
    EMPLOYEE("employee"),
    MANAGER("manager"),
    TOP_MANAGER("top_manager");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PositionEnum fromValue(String value) {
        for (PositionEnum b : PositionEnum.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
