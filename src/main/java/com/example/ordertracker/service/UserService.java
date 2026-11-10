package com.example.ordertracker.service;

import com.example.ordertracker.entity.User;
import com.example.ordertracker.exception.BusinessException;
import com.example.ordertracker.exception.ExceptionConstants;
import com.example.ordertracker.mapper.OrderMapper;
import com.example.ordertracker.mapper.UserMapper;
import com.example.ordertracker.model.request.UpdateProfileRequest;
import com.example.ordertracker.model.response.OrderResponse;
import com.example.ordertracker.model.response.UserResponse;
import com.example.ordertracker.repository.OrderRepository;
import com.example.ordertracker.repository.UserRepository;
import com.example.ordertracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userResponseMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public UserResponse getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
        return userResponseMapper.toUserResponse(user);
    }

    public UserResponse updateMyProfile(UpdateProfileRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));

        if (request.name() != null && !request.name().isEmpty()) {
            user.setName(request.name());
        }

        if (request.password() != null && !request.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        User updatedUser = userRepository.save(user);

        return userResponseMapper.toUserResponse(updatedUser);
    }

    public void deleteMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
        orderRepository.deleteAll(user.getOrders());
        userRepository.delete(user);
    }

    public List<OrderResponse> getMyOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
        if (user.getOrders().isEmpty()) return List.of();
        return orderMapper.toOrderResponseList(user.getOrders());
    }
}
