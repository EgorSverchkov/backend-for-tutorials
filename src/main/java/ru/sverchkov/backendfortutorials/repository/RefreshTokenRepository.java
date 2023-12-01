package ru.sverchkov.backendfortutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.sverchkov.backendfortutorials.model.entity.RefreshTokenEntity;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findFirstByTokenOrderByExpiryDateDesc(String token);

    @Modifying
    int deleteByUser(UserEntity user);
}
