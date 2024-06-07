package com.prokofeva.calculator_api.doman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmploymentStatus {
    UNEMPLOYED("unemployed"),
    SELF_EMPLOYED("self employed"),
    EMPLOYED("employed"),
    BUSINESS_OWNER("business owner");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EmploymentStatus fromValue(String value) {
        for (EmploymentStatus b : EmploymentStatus.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
