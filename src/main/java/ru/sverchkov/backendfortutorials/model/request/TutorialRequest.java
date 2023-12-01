package ru.sverchkov.backendfortutorials.model.request;

import lombok.Data;

@Data
public class TutorialRequest {
    private String title;
    private String description;
    private String shortText;
    private String image;
    private String text;
    private boolean published;
}
