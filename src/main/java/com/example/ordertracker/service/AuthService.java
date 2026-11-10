package com.example.ordertracker.service;

import com.example.ordertracker.entity.Authority;
import com.example.ordertracker.entity.User;
import com.example.ordertracker.exception.BusinessException;
import com.example.ordertracker.exception.ExceptionConstants;
import com.example.ordertracker.model.request.LoginRequest;
import com.example.ordertracker.model.request.RegisterRequest;
import com.example.ordertracker.model.request.TokenRequest;
import com.example.ordertracker.model.response.TokenResponse;
import com.example.ordertracker.repository.UserRepository;
import com.example.ordertracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService  {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;



    public TokenResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BusinessException(ExceptionConstants.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .status("ACTIVE")
                .build();

        Authority userAuth = Authority.builder()
                .authority("ROLE_USER")
                .user(user)
                .build();

        user.setAuthorities(new HashSet<>(List.of(userAuth)));

        userRepository.save(user);

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));

        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refreshToken(TokenRequest request) {
        String reqRefreshToken = request.refreshToken();
        String username = jwtUtil.extractUsername(reqRefreshToken);
        
        if (username != null) {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new BusinessException(ExceptionConstants.USER_NOT_FOUND));
            
            if (jwtUtil.validateToken(reqRefreshToken, user.getEmail())) {
                String newAccessToken = jwtUtil.generateToken(user.getEmail());
                return new TokenResponse(newAccessToken, reqRefreshToken);
            }
        }
        throw new BusinessException(ExceptionConstants.INVALID_REFRESH_TOKEN);
    }
}
