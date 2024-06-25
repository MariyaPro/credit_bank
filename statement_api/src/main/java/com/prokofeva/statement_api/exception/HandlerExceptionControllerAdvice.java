package com.prokofeva.statement_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class HandlerExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<List<ValidationError>> handleValidationRequestException(MethodArgumentNotValidException e) {
        final List<ValidationError> errorsList = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.error("Некорректные данные в запросе, перехвачено исключение MethodArgumentNotValidException.class : {}", errorsList);
        return ResponseEntity.badRequest().body(errorsList);
    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseBody
    public ResponseEntity<String> handleExternalServiceError(ExternalServiceException e) {
        log.error("Перехвачено исключение ExternalServiceException.class : {}", e.getMessage());
        return ResponseEntity.status(406).body(e.getMessage());
    }

    @ExceptionHandler(DeniedLoanException.class)
    @ResponseBody
    public ResponseEntity<String> handleDeniedLoanException(DeniedLoanException e) {
        log.error("Перехвачено исключение DeniedLoanException.class : {}", e.getMessage());
        return ResponseEntity.ok(e.getMessage());
    }

}
