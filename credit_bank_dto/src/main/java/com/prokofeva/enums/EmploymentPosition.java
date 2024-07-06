package com.prokofeva.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmploymentPosition {
    WORKER("worker"),
    MID_MANAGER("mid manager"),
    TOP_MANAGER("top manager"),
    OWNER("owner");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EmploymentPosition fromValue(String value) {
        for (EmploymentPosition b : EmploymentPosition.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}