package com.example.ordertracker.model.request;

public record UpdateProfileRequest(
    String name,
    String password
) {}
