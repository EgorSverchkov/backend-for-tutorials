package ru.sverchkov.backendfortutorials.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.jwt.JwtUtils;
import ru.sverchkov.backendfortutorials.model.dto.UserDetailsImpl;
import ru.sverchkov.backendfortutorials.model.entity.RefreshTokenEntity;
import ru.sverchkov.backendfortutorials.model.request.LoginRequest;
import ru.sverchkov.backendfortutorials.model.response.JwtResponse;
import ru.sverchkov.backendfortutorials.service.LoginUserService;
import ru.sverchkov.backendfortutorials.service.RefreshTokenService;

import java.util.List;

@Service
@Slf4j
public class LoginUserServiceImpl implements LoginUserService {
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public LoginUserServiceImpl(JwtUtils jwtUtils, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        log.info("authUser method called with loginRequest : {}", loginRequest);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        log.info("authentication is : {}", authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshTokenEntity jwtTokenRefresh = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(
                jwtToken
                , jwtTokenRefresh.getToken()
                , userDetails.getId()
                , userDetails.getUsername()
                , userDetails.getEmail()
                , roles));
    }
}
