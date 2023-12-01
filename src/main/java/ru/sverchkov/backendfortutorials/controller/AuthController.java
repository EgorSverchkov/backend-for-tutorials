package ru.sverchkov.backendfortutorials.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sverchkov.backendfortutorials.model.request.LoginRequest;
import ru.sverchkov.backendfortutorials.model.request.SignupRequest;
import ru.sverchkov.backendfortutorials.model.request.TokenRefreshRequest;
import ru.sverchkov.backendfortutorials.service.LoginUserService;
import ru.sverchkov.backendfortutorials.service.RefreshTokenService;
import ru.sverchkov.backendfortutorials.service.RegisterUserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {
    private final RefreshTokenService refreshTokenService;

    private final RegisterUserService registerUserService;

    private final LoginUserService loginUserService;

    public AuthController(RefreshTokenService refreshTokenService
            , RegisterUserService registerUserService
            , LoginUserService loginUserService) {
        this.refreshTokenService = refreshTokenService;
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@Valid @RequestBody LoginRequest loginRequest) {
        return loginUserService.loginUser(loginRequest);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        return registerUserService.registerUser(signupRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody @Valid TokenRefreshRequest request) {
        log.info("Dispatch /auth/refresh path with request : " + request);
        return ResponseEntity.ok(refreshTokenService.refreshToken(request));
    }

}
