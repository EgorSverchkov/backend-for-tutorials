package ru.sverchkov.backendfortutorials.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sverchkov.backendfortutorials.model.entity.FeedbackEntity;
import ru.sverchkov.backendfortutorials.model.entity.TutorialEntity;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {
    List<FeedbackEntity> findAllByUserent(UserEntity userent);
    List<FeedbackEntity> findAllByTutorial(TutorialEntity tutorialEntity);
    List<FeedbackEntity> findAllByIsActiveTrue();
}
