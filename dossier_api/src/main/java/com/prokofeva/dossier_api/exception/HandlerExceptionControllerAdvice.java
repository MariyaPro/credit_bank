package com.prokofeva.dossier_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class HandlerExceptionControllerAdvice {

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseBody
    public ResponseEntity<String> handleExternalServiceError(ExternalServiceException e) {
        log.error("Перехвачено исключение ExternalServiceException.class : {}", e.getMessage());
        return ResponseEntity.status(406).body(e.getMessage());
    }
}