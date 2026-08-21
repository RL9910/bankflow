package com.bankflow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;

@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RateLimitService rateLimitService;

    public AuthController(
        AuthService authService, 
        JwtService jwtService,
        RateLimitService rateLimitService
        ) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/auth/register")
    public UserResponse register(@RequestBody RegisterRequest request) {
        User user = authService.register(
            request.getEmail(),
            request.getPassword()
        );

        return new UserResponse(
            user.getId(),
            user.getEmail()
        );

    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (rateLimitService.isBlocked(request.getEmail())) {
            return ResponseEntity
                .status(429)
                .body("Too many login attempts. Try again later.");
        }


        try {

            User user = authService.login(
                request.getEmail(),
                request.getPassword()
            );

            rateLimitService.resetAttempts(request.getEmail());

            String token = jwtService.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));


        } catch (Exception e) {

            rateLimitService.recordFailedAttempt(
                request.getEmail()
            );

            throw e;
        }

       
    }



}