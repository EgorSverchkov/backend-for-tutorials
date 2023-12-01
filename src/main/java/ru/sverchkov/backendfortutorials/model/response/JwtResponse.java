package ru.sverchkov.backendfortutorials.model.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, String refreshToken, UUID id, String username, String email, List<String> roles) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
