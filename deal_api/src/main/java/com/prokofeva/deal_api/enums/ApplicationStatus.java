package com.prokofeva.deal_api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApplicationStatus {
    PREAPPROVAL("preapproval"),
    APPROVED("approved"),
    CC_DENIED("cc denied"),
    CC_APPROVED("cc approved"),
    PREPARE_DOCUMENTS("prepare documents"),
    DOCUMENT_CREATED("document created"),
    CLIENT_DENIED("client denied"),
    DOCUMENT_SIGNED("document signed"),
    CREDIT_ISSUED("credit issued");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ApplicationStatus fromValue(String value) {
        for (ApplicationStatus b : ApplicationStatus.values()) {
            if (b.value.equalsIgnoreCase(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
