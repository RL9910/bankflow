package com.bankflow;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {


    private static final String SECRET =
        "bankflow-super-secret-jwt-signing-key-123456789";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
            SECRET.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateToken(User user) {
        Date now = new Date();

        Date expiration = new Date(
            now.getTime() + 60 * 60 * 1000
        );

        return Jwts.builder()
            .subject(user.getEmail())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(getSigningKey())
            .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    // public boolean isTokenValid(String token, User user) {
    //     ...
    // }

}