package ru.sverchkov.backendfortutorials.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.sverchkov.backendfortutorials.exception.EmptyTutorialIdException;
import ru.sverchkov.backendfortutorials.exception.EmptyTutorialRequestException;
import ru.sverchkov.backendfortutorials.exception.TutorialExistException;
import ru.sverchkov.backendfortutorials.model.entity.TutorialEntity;
import ru.sverchkov.backendfortutorials.model.entity.TutorialText;
import ru.sverchkov.backendfortutorials.model.request.TutorialRequest;
import ru.sverchkov.backendfortutorials.model.response.TutorialResponse;
import ru.sverchkov.backendfortutorials.repository.TutorialRepository;
import ru.sverchkov.backendfortutorials.service.TutorialService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TutorialServiceImpl implements TutorialService {
    private static final String TUTORIAL_EXIST = "Tutorial with this id is exist";
    private static final String TUTORIAL_ID_NULL = "Id is null";
    private static final String TUTORIAL_REQUEST_IS_NULL = "Tutorial request is null";

    private final TutorialRepository tutorialRepository;

    public TutorialServiceImpl(TutorialRepository tutorialRepository) {
        this.tutorialRepository = tutorialRepository;
    }

    @Override
    public ArrayList<TutorialResponse> getAllTutorials() {
        ArrayList<TutorialResponse> tutorialResponses = new ArrayList<>();
        tutorialRepository
                .findAll()
                .forEach(tutor -> tutorialResponses.add(buildTutorialResponseFromEntity(tutor)));
        return tutorialResponses;
    }

    @Override
    public TutorialEntity getTutorialById(UUID id) {
        if(id == null) throw new EmptyTutorialIdException(TUTORIAL_ID_NULL);
        return tutorialRepository
                .findById(id)
                .orElseThrow(() -> new TutorialExistException(TUTORIAL_EXIST));
    }

    @Override
    @Transactional
    public TutorialEntity createTutorial(TutorialRequest request) {
        if(request == null) throw new EmptyTutorialRequestException(TUTORIAL_REQUEST_IS_NULL);
        return tutorialRepository.saveAndFlush(TutorialEntity.builder()
                        .title(request.getTitle())
                        .shortText(request.getShortText())
                        .image(request.getImage())
                        .description(request.getDescription())
                        .fullText(TutorialText.builder()
                                .text(request.getText())
                                .build())
                        .published(request.isPublished())
                .build());
    }

    @Override
    @Transactional
    public TutorialEntity updateTutorial(UUID id, TutorialRequest request) {
        if(id == null) throw new EmptyTutorialIdException(TUTORIAL_ID_NULL);

        if(request == null) throw new EmptyTutorialRequestException(TUTORIAL_REQUEST_IS_NULL);

        TutorialEntity tutorialEntity = tutorialRepository
                .findById(id).orElseThrow(() -> new TutorialExistException(TUTORIAL_EXIST));

        if(!request.getDescription().isEmpty()) {
            tutorialEntity.setDescription(request.getDescription());
        }

        if(!request.getTitle().isEmpty()) {
            tutorialEntity.setTitle(request.getTitle());
        }

        if(!request.getImage().isEmpty()) {
            tutorialEntity.setImage(request.getImage());
        }

        if(!request.getShortText().isEmpty()) {
            tutorialEntity.setShortText(request.getShortText());
        }

        if(request.getText() != null && !request.getText().isEmpty()) {
            tutorialEntity.setFullText(TutorialText.builder()
                            .text(request.getText())
                    .build());
        }

        tutorialEntity.setPublished(request.isPublished());

        return tutorialRepository.saveAndFlush(tutorialEntity);
    }

    @Override
    @Transactional
    public HttpStatus deleteTutorial(UUID id) {
        if(id == null) throw new EmptyTutorialIdException(TUTORIAL_ID_NULL);

        tutorialRepository.deleteById(id);

        return HttpStatus.OK;
    }

    @Override
    public HttpStatus deleteAllTutorials() {
        tutorialRepository.deleteAll();

        return HttpStatus.OK;
    }

    @Override
    @Transactional
    public List<TutorialEntity> findByPublished() {
        return tutorialRepository.findByPublished(true);
    }

    private TutorialResponse buildTutorialResponseFromEntity(TutorialEntity tutor) {
        return TutorialResponse.builder()
                .id(tutor.getId().toString())
                .createdAt(tutor.getCreatedAt().toString())
                .updatedAt(tutor.getUpdatedAt().toString())
                .title(tutor.getTitle())
                .shortText(tutor.getShortText())
                .description(tutor.getDescription())
                .fullText(tutor.getFullText() == null ? "" : tutor.getFullText().getText())
                .published(tutor.isPublished())
                .image(tutor.getImage())
                .build();
    }
}
