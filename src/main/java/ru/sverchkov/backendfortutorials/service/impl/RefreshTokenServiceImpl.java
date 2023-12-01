package ru.sverchkov.backendfortutorials.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.exception.TokenRefreshException;
import ru.sverchkov.backendfortutorials.jwt.JwtUtils;
import ru.sverchkov.backendfortutorials.model.entity.RefreshTokenEntity;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;
import ru.sverchkov.backendfortutorials.model.request.TokenRefreshRequest;
import ru.sverchkov.backendfortutorials.model.response.TokenRefreshResponse;
import ru.sverchkov.backendfortutorials.repository.RefreshTokenRepository;
import ru.sverchkov.backendfortutorials.repository.UserRepository;
import ru.sverchkov.backendfortutorials.service.RefreshTokenService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${acf.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenServiceImpl(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public RefreshTokenEntity createRefreshToken(UUID userId) {
        log.info("createRefreshToken method was called with userId : "  + userId.toString());
        UserEntity userEntity = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User with id= " + userId + " not found."));
        log.info("UserEntity is : " + userEntity);
        RefreshTokenEntity build = RefreshTokenEntity.builder()
                .user(userEntity)
                .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))
                .token(UUID.randomUUID().toString())
                .build();
        log.info("RefreshTokenEntity is : " + build);
        return refreshTokenRepository.saveAndFlush(build);
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        log.info("findByToken method was called with token : " + token);

        return refreshTokenRepository
                .findFirstByTokenOrderByExpiryDateDesc(token);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        log.info("verifyExpiration method was called with token expirity : " + token.getExpiryDate());
        log.info("Instant now : " + Instant.now());
        log.info("exp and instant compare : " + token.getExpiryDate().compareTo(Instant.now()));
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(
                    token.getToken()
                    , "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Override
    @Transactional
    public int deleteByUser(UUID id) {
        log.info("deleteByUser method was called with UUID : " + id.toString());
        return refreshTokenRepository.deleteByUser(userRepository.findById(id)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with id= " + id + " not found.")));
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        if(request == null) throw new TokenRefreshException("", "Error! Token request is empty!");
        log.info("refreshToken method was called with request : " + request);
        String refreshToken = request.getRefreshToken();
        if(refreshToken == null) throw new TokenRefreshException("", "Error! Token is empty!");
        log.info("refreshToken is : " + refreshToken);

        return findByToken(refreshToken)
                .map(this::verifyExpiration)
                .map(RefreshTokenEntity::getUser)
                .map(user -> {
                    log.info("user is : " + user);
                    String token = jwtUtils.generateJwtToken(user.getUsername());
                    log.info("new token is : " + token);
                    return new TokenRefreshResponse(token, refreshToken);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token exist in database."));
    }
}
