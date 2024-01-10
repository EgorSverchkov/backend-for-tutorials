package ru.sverchkov.backendfortutorials.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
public class FeedbackRequest {
    @NotEmpty
    private String feedbackText;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String tutorialId;

    @NotEmpty
    private boolean isActive;

    @NotEmpty
    private Integer grade;

}
