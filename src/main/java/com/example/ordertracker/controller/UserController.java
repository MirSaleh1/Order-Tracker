package com.example.ordertracker.controller;

import com.example.ordertracker.model.request.UpdateProfileRequest;
import com.example.ordertracker.model.response.OrderResponse;
import com.example.ordertracker.model.response.UserResponse;
import com.example.ordertracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyProfile() {
        userService.deleteMyProfile();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        return ResponseEntity.ok(userService.getMyOrders());
    }
}
