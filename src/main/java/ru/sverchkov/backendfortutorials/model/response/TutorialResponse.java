package ru.sverchkov.backendfortutorials.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TutorialResponse {
    private String id;
    private String title;
    private String image;
    private String shortText;
    private String description;
    private String fullText;
    private boolean published;
    private String createdAt;
    private String updatedAt;
}
