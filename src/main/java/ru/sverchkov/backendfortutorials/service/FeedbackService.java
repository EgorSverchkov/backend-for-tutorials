package ru.sverchkov.backendfortutorials.service;

import ru.sverchkov.backendfortutorials.model.request.FeedbackRequest;
import ru.sverchkov.backendfortutorials.model.response.FeedbackResponse;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse createFeedback(FeedbackRequest request);

    List<FeedbackResponse> getAll();

    List<FeedbackResponse> getAllByTutorial(String tutorialId);

    List<FeedbackResponse> getAllByUser(String userId);

    List<FeedbackResponse> getAllActive();

    String deleteById(String feedbackId);
}
