package com.prokofeva.statement_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationError {
    private String fieldName;
    private String message;
}