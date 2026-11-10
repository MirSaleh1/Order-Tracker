package com.example.ordertracker.controller;

import com.example.ordertracker.model.request.LoginRequest;
import com.example.ordertracker.model.request.RegisterRequest;
import com.example.ordertracker.model.request.TokenRequest;
import com.example.ordertracker.model.response.TokenResponse;
import com.example.ordertracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@Valid @RequestBody TokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
}
