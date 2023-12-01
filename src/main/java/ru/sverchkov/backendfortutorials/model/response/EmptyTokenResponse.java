package ru.sverchkov.backendfortutorials.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class EmptyTokenResponse {
    private String message;
    private int statusCode;
    private Date timestamp;
}
