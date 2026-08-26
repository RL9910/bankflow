package com.bankflow.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bankflow.user.UserRepository;
import com.bankflow.user.User;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(String email, String password) {
        
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(email, hashedPassword);
        
        return userRepository.save(user);

    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
            .orElseThrow(() ->
                new InvalidCredentialsException("Invalid email or password")
            );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException(
                "Invalid email or password"
            );
    }

        return user;
        
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

}