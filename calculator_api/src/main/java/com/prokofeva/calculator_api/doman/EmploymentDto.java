package com.prokofeva.calculator_api.doman;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class EmploymentDto {
    @NotNull
    private EmploymentStatusEnum employmentStatus;

    @NotNull
    private String employerINN;

    @NotNull
    private BigDecimal salary;

    @NotNull
    private PositionEnum position;

    @NotNull
    private Integer workExperienceTotal;

    @NotNull
    private Integer workExperienceCurrent;

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
}

