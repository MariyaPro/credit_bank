package com.prokofeva.statement_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeniedLoanException extends RuntimeException {

    private final String message;
}
