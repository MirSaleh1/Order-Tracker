package com.example.ordertracker.model.response;

public record TokenResponse(
    String accessToken,
    String refreshToken
) {
}
