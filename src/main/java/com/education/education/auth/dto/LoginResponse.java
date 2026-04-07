package com.education.education.auth.dto;

import com.education.education.user.dto.UserResponse;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private UserResponse user;

    public LoginResponse(String accessToken, String refreshToken, UserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public UserResponse getUser() { return user; }
}
