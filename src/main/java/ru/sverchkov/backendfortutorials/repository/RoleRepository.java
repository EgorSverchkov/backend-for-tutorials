package ru.sverchkov.backendfortutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sverchkov.backendfortutorials.model.dto.ERole;
import ru.sverchkov.backendfortutorials.model.entity.RoleEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    Optional<RoleEntity> findByName(ERole name);
}
