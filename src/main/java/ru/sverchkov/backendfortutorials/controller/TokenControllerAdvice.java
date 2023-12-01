package ru.sverchkov.backendfortutorials.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.sverchkov.backendfortutorials.exception.EmptyTokenException;
import ru.sverchkov.backendfortutorials.exception.TokenRefreshException;
import ru.sverchkov.backendfortutorials.model.response.EmptyTokenResponse;
import ru.sverchkov.backendfortutorials.model.response.ErrorMessage;

import java.util.Date;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenRefreshException tokenRefreshException, WebRequest request) {
        return new ErrorMessage(HttpStatus.FORBIDDEN.value(),
                new Date(),
                tokenRefreshException.getMessage(),
                request.getDescription(false));
    }

    @ExceptionHandler(EmptyTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<EmptyTokenResponse> handleEmptyTokenException(EmptyTokenException exception) {
        return ResponseEntity.badRequest().body(EmptyTokenResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(exception.getMessage())
                        .timestamp(new Date())
                .build());
    }
}
