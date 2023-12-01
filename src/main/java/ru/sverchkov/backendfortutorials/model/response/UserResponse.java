package ru.sverchkov.backendfortutorials.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private String avatar;
    private String birthday;
    private List<String> roles;
}
