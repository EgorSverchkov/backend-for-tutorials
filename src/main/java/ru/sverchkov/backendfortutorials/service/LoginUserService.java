package ru.sverchkov.backendfortutorials.service;

import org.springframework.http.ResponseEntity;
import ru.sverchkov.backendfortutorials.model.request.LoginRequest;

public interface LoginUserService {
    ResponseEntity<?> loginUser(LoginRequest loginRequest);
}
