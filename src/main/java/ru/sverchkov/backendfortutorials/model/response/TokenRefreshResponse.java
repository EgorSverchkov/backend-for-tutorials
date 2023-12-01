package ru.sverchkov.backendfortutorials.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRefreshResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";

    public TokenRefreshResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
