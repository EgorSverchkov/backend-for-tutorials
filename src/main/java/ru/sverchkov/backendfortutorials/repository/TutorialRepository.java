package ru.sverchkov.backendfortutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sverchkov.backendfortutorials.model.entity.TutorialEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface TutorialRepository extends JpaRepository<TutorialEntity, UUID> {
    List<TutorialEntity> findByPublished(boolean published);
}
