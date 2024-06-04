package com.prokofeva.calculator_api.doman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmploymentStatusEnum {
    UNEMPLOYED("unemployed"),
    BUSY("busy"),
    SELF_EMPLOYED("self_employed"),
    BUSINESS_OWNER("business_owner");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static EmploymentStatusEnum fromValue(String value) {
        for (EmploymentStatusEnum b : EmploymentStatusEnum.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
