package com.connector.gitcon.service;

import com.connector.gitcon.entity.User;
import com.connector.gitcon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(String username, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        userRepository.save(user);

        return jwtService.generateToken(
                user.getId(),
                user.getUsername());
    }

    public String login(String username, String password) {

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return jwtService.generateToken(
                user.getId(),
                user.getUsername());
    }
}
