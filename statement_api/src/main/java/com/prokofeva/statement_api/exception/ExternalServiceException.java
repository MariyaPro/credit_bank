package com.prokofeva.statement_api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExternalServiceException extends RuntimeException {
    private final String message;
}
