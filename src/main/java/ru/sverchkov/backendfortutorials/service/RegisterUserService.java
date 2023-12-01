package ru.sverchkov.backendfortutorials.service;

import org.springframework.http.ResponseEntity;
import ru.sverchkov.backendfortutorials.model.request.SignupRequest;

public interface RegisterUserService {
    ResponseEntity<?> registerUser(SignupRequest signupRequest);
}
