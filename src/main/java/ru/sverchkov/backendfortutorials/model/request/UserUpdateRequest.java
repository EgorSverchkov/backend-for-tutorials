package ru.sverchkov.backendfortutorials.model.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String avatar;
    private String birthday;
}
