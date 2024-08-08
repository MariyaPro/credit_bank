package com.prokofeva.calculator_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationError {
    private String fieldName;
    private String message;
}
