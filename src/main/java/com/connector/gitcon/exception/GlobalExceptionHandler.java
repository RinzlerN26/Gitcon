package com.connector.gitcon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.connector.gitcon.dto.response.ErrorResponse;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GithubApiException.class)
    public ResponseEntity<?> handleGithubException(GithubApiException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.BAD_GATEWAY.value(),
                        ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Something went wrong"));
    }

    @ExceptionHandler(CustomServiceException.class)
    public ResponseEntity<?> handleCustomException(CustomServiceException ex) {
        HttpStatus status = ex.getStatus();

        return ResponseEntity.status(status).body(
                new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        ex.getMessage()));
    }
}