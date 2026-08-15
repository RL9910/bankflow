package com.bankflow;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
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
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = authService.login(
            request.getEmail(),
            request.getPassword()
        );

        String token = jwtService.generateToken(user);

        return new LoginResponse(token);

    }



}