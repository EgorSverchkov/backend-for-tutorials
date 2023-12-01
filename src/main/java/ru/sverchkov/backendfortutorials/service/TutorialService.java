package ru.sverchkov.backendfortutorials.service;

import org.springframework.http.HttpStatus;
import ru.sverchkov.backendfortutorials.model.entity.TutorialEntity;
import ru.sverchkov.backendfortutorials.model.request.TutorialRequest;
import ru.sverchkov.backendfortutorials.model.response.TutorialResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface TutorialService {
    ArrayList<TutorialResponse> getAllTutorials();
    TutorialEntity getTutorialById(UUID id);
    TutorialEntity createTutorial(TutorialRequest request);
    TutorialEntity updateTutorial(UUID id,TutorialRequest request);
    HttpStatus deleteTutorial(UUID id);
    HttpStatus deleteAllTutorials();
    List<TutorialEntity> findByPublished();

}
