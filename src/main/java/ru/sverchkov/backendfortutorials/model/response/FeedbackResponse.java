package ru.sverchkov.backendfortutorials.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FeedbackResponse {
    private UUID id;
    private String feedbackText;
    private Boolean isActive;
    private Integer grade;
    private LocalDateTime createdAt;
    private UUID userId;
    private UUID tutorialId;
    private String tutorialTitle;
}
