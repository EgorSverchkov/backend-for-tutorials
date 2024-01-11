package ru.sverchkov.backendfortutorials.service;

import ru.sverchkov.backendfortutorials.model.entity.FeedbackEntity;
import ru.sverchkov.backendfortutorials.model.request.FeedbackRequest;

import java.util.List;

public interface FeedbackService {
    FeedbackEntity createFeedback(FeedbackRequest request);
    List<FeedbackEntity> getAll();
    List<FeedbackEntity> getAllByTutorial(String tutorialId);
    List<FeedbackEntity> getAllByUser(String userId);
    List<FeedbackEntity> getAllActive();

    String deleteById(String feedbackId);
}
