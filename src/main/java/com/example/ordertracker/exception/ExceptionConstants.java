package com.example.ordertracker.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionConstants {
    USER_NOT_FOUND("User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("User already exists", HttpStatus.CONFLICT),
    INVALID_REFRESH_TOKEN("Invalid refresh token", HttpStatus.UNAUTHORIZED),
    USER_CANNOT_BE_NULL("User cannot be null", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND("Order not found", HttpStatus.NOT_FOUND),
    LOG_NOT_FOUND("Log not found", HttpStatus.NOT_FOUND);;

    private final String message;
    private final HttpStatus status;
}
