package com.example.ordertracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret:defaultSecretKeyForDevelopmentOnlyAtLeast64BytesLong12345678901234567890}")
    private String secret;

    @Value("${jwt.expiration-time:3600000}")
    private long expirationTime;

    @Value("${jwt.refresh-expiration-time:604800000}")
    private long refreshExpirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "ACCESS");
        return createToken(claims, username, expirationTime);
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "REFRESH");
        return createToken(claims, username, refreshExpirationTime);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMillis) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public Boolean isAccessToken(String token) {
        String tokenType = extractClaim(token, claims -> claims.get("token_type", String.class));
        return "ACCESS".equals(tokenType);
    }

    public Boolean isRefreshToken(String token) {
        String tokenType = extractClaim(token, claims -> claims.get("token_type", String.class));
        return "REFRESH".equals(tokenType);
    }
}
