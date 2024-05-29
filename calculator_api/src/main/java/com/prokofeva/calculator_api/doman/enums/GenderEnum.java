package com.prokofeva.calculator_api.doman.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.prokofeva.calculator_api.doman.dto.ScoringDataDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum GenderEnum {
    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static GenderEnum fromValue(String value) {
        for (GenderEnum b : GenderEnum.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
