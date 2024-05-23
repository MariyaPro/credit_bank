package com.prokofeva.calculator_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DeniedLoanException extends RuntimeException {

    private final String message;
}
