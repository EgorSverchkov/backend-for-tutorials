package ru.sverchkov.backendfortutorials.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.sverchkov.backendfortutorials.exception.EmptyRequestException;
import ru.sverchkov.backendfortutorials.exception.TutorialExistException;
import ru.sverchkov.backendfortutorials.model.entity.FeedbackEntity;
import ru.sverchkov.backendfortutorials.model.entity.TutorialEntity;
import ru.sverchkov.backendfortutorials.model.entity.UserEntity;
import ru.sverchkov.backendfortutorials.model.request.FeedbackRequest;
import ru.sverchkov.backendfortutorials.model.response.FeedbackResponse;
import ru.sverchkov.backendfortutorials.repository.FeedbackRepository;
import ru.sverchkov.backendfortutorials.repository.TutorialRepository;
import ru.sverchkov.backendfortutorials.repository.UserRepository;
import ru.sverchkov.backendfortutorials.service.FeedbackService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Transactional
public class FeedbackServiceImpl implements FeedbackService {
    private static final String FEEDBACK_DELETED = "Отзыв удален!";
    private static final String EMPTY_MESSAGE = "Ошибка! \nПустой запрос.";
    private static final String USER_EXIST = "Пользователя с таким id нет в системе.";
    private static final String TUTORIAL_EXIST = "Курс с таким id отсутствует в системе.";

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final TutorialRepository tutorialRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository, UserRepository userRepository, TutorialRepository tutorialRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
        this.tutorialRepository = tutorialRepository;
    }

    @Override
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        if(request == null) throw new EmptyRequestException(EMPTY_MESSAGE);

        UserEntity userById = userRepository
                .findById(UUID.fromString(request.getUserId()))
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXIST));

        TutorialEntity tutorialById = tutorialRepository
                .findById(UUID.fromString(request.getTutorialId()))
                .orElseThrow(() -> new TutorialExistException(TUTORIAL_EXIST));

        FeedbackEntity newFeedback = FeedbackEntity
                .builder()
                .feedbackText(request.getFeedbackText())
                .isActive(request.isActive())
                .userent(userById)
                .tutorial(tutorialById)
                .grade(request.getGrade())
                .build();

        FeedbackEntity feedbackEntity = feedbackRepository.saveAndFlush(newFeedback);
        return toResponse(feedbackEntity);
    }

    @Override
    public List<FeedbackResponse> getAll() {
        ArrayList<FeedbackResponse> feedbackResponses = new ArrayList<>();
        feedbackRepository.findAll().forEach(entity -> feedbackResponses.add(toResponse(entity)));
        return feedbackResponses;
    }

    @Override
    public List<FeedbackResponse> getAllByTutorial(String tutorialId) {
        if(tutorialId == null || tutorialId.isEmpty()) throw new EmptyRequestException(EMPTY_MESSAGE);

        TutorialEntity tutorialEntityFromTutorialId = tutorialRepository
                .findById(UUID.fromString(tutorialId))
                .orElseThrow(() -> new TutorialExistException(TUTORIAL_EXIST));

        ArrayList<FeedbackResponse> feedbackResponses = new ArrayList<>();
        feedbackRepository
                .findAllByTutorial(tutorialEntityFromTutorialId)
                .forEach(entity -> feedbackResponses.add(toResponse(entity)));
        return feedbackResponses;
    }

    @Override
    public List<FeedbackResponse> getAllByUser(String userId) {
        if(userId == null || userId.isEmpty()) throw new EmptyRequestException(EMPTY_MESSAGE);

        UserEntity userByUserId = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXIST));

        ArrayList<FeedbackResponse> feedbackResponses = new ArrayList<>();
        feedbackRepository
                .findAllByUserent(userByUserId)
                .forEach(entity -> feedbackResponses.add(toResponse(entity)));

        return feedbackResponses;
    }

    @Override
    public List<FeedbackResponse> getAllActive() {
        ArrayList<FeedbackResponse> feedbackResponses = new ArrayList<>();
        feedbackRepository
                .findAllByIsActiveTrue()
                .forEach(entity -> feedbackResponses.add(toResponse(entity)));
        return feedbackResponses;
    }

    @Override
    public String deleteById(String feedbackId) {
        feedbackRepository.deleteById(UUID.fromString(feedbackId));
        return FEEDBACK_DELETED;
    }

    private FeedbackResponse toResponse(FeedbackEntity entity) {
        return FeedbackResponse.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .feedbackText(entity.getFeedbackText())
                .grade(entity.getGrade())
                .userId(entity.getUserent().getId())
                .isActive(entity.getIsActive())
                .tutorialId(entity.getTutorial().getId())
                .tutorialTitle(entity.getTutorial().getTitle())
                .build();
    }
}
