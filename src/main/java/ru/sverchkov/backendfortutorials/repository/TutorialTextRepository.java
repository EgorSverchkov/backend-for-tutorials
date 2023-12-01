package ru.sverchkov.backendfortutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sverchkov.backendfortutorials.model.entity.TutorialText;

import java.util.UUID;

@Repository
public interface TutorialTextRepository extends JpaRepository<TutorialText, UUID> {
}
