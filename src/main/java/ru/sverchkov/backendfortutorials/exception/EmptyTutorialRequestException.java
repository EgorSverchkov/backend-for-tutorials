package ru.sverchkov.backendfortutorials.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyTutorialRequestException extends RuntimeException {
    public EmptyTutorialRequestException(String message) {
        super(message);
    }
}
