package com.prokofeva.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MaritalStatus {
    MARRIED("married"),
    DIVORCED("divorced"),
    SINGLE("single"),
    WIDOW_WIDOWER("widow widower");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static MaritalStatus fromValue(String value) {
        for (MaritalStatus b : MaritalStatus.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}