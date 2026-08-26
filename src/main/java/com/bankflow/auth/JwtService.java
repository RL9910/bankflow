package com.bankflow.auth;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.io.Decoders;

import com.bankflow.user.User;

@Service
public class JwtService {

    private final String secretKey;

    public JwtService(
            @Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
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