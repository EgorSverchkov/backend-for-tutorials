package ru.sverchkov.backendfortutorials.service;

import ru.sverchkov.backendfortutorials.model.entity.RefreshTokenEntity;
import ru.sverchkov.backendfortutorials.model.request.TokenRefreshRequest;
import ru.sverchkov.backendfortutorials.model.response.TokenRefreshResponse;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenService {
    RefreshTokenEntity createRefreshToken(UUID userId);

    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    int deleteByUser(UUID id);

    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
}
