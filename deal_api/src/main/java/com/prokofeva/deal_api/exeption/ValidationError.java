package com.prokofeva.deal_api.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationError {
    private String fieldName;
    private String message;
}