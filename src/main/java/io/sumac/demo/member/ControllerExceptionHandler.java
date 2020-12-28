package io.sumac.demo.member;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return standardClientErrorResponse(ClientError.builder().reason("Request has validation errors").validationErrors(errors).build(), HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(ApplicationException.NotFoundException e) {
        return standardClientErrorResponse(e.getHttpStatus(), e);
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(ApplicationException e) {
        var body = ClientError.builder().reason(e.getMessage()).build();
        return standardClientErrorResponse(body, e.getHttpStatus(), e);
    }

    private ResponseEntity<?> standardClientErrorResponse(ClientError body, HttpStatus httpStatus, Exception e) {
        log.info("Client error. Returning status {}. Reason: {}", httpStatus, e.toString());
        return ResponseEntity.status(httpStatus).body(body);
    }

    private ResponseEntity<?> standardClientErrorResponse(HttpStatus httpStatus, Exception e) {
        log.info("Client error. Returning status {}. Reason: {}", httpStatus, e.toString());
        return ResponseEntity.status(httpStatus).build();
    }

    @Data
    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClientError {
        private String reason;
        @Builder.Default
        private Map<String, String> validationErrors = new HashMap<>();
    }

}
