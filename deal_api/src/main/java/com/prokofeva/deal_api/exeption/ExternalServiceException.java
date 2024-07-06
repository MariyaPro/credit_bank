package com.prokofeva.deal_api.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalServiceException extends  RuntimeException{

    private final String message;
}