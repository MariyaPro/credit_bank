package com.prokofeva.deal_api.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class HandlerExceptionControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<List<ValidationError>> handleValidationRequestException(MethodArgumentNotValidException e) {
        final List<ValidationError> errorsList = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorsList);
    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseBody
    public ResponseEntity<String> handleExternalServiceError(ExternalServiceException e) {
        return ResponseEntity.status(406).body(e.getMessage());
    }


}
