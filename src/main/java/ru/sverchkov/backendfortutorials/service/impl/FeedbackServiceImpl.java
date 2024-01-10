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
import ru.sverchkov.backendfortutorials.repository.FeedbackRepository;
import ru.sverchkov.backendfortutorials.repository.TutorialRepository;
import ru.sverchkov.backendfortutorials.repository.UserRepository;
import ru.sverchkov.backendfortutorials.service.FeedbackService;

import java.util.List;
import java.util.UUID;

@Component
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
    @Transactional
    public FeedbackEntity createFeedback(FeedbackRequest request) {
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
                .user(userById)
                .tutorial(tutorialById)
                .grade(request.getGrade())
                .build();
        return feedbackRepository.saveAndFlush(newFeedback);
    }

    @Override
    public List<FeedbackEntity> getAll() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<FeedbackEntity> getAllByTutorial(String tutorialId) {
        if(tutorialId == null || tutorialId.isEmpty()) throw new EmptyRequestException(EMPTY_MESSAGE);

        TutorialEntity tutorialEntityFromTutorialId = tutorialRepository
                .findById(UUID.fromString(tutorialId))
                .orElseThrow(() -> new TutorialExistException(TUTORIAL_EXIST));

        return feedbackRepository.findAllByTutorial(tutorialEntityFromTutorialId);
    }

    @Override
    public List<FeedbackEntity> getAllByUser(String userId) {
        if(userId == null || userId.isEmpty()) throw new EmptyRequestException(EMPTY_MESSAGE);

        UserEntity userByUserId = userRepository
                .findById(UUID.fromString(userId))
                .orElseThrow(() -> new UsernameNotFoundException(USER_EXIST));

        return feedbackRepository.findAllByUser(userByUserId);
    }

    @Override
    public List<FeedbackEntity> getAllActive() {
        return feedbackRepository.findAllByIsActiveTrue();
    }

    @Override
    @Transactional
    public String deleteById(String feedbackId) {
        feedbackRepository.deleteById(UUID.fromString(feedbackId));
        return FEEDBACK_DELETED;
    }
}
