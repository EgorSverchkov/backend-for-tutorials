package ru.sverchkov.backendfortutorials.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MessageResponse {
    private String message;
}
