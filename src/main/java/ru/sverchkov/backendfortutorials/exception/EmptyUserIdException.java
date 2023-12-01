package ru.sverchkov.backendfortutorials.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmptyUserIdException extends RuntimeException {
    public EmptyUserIdException() {
        super("User id is empty");
    }
}
