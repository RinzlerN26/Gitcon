package com.connector.gitcon.service;

import com.connector.gitcon.entity.User;
import com.connector.gitcon.exception.CustomServiceException;
import com.connector.gitcon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(String username, String password) {

        if (userRepository.existsByUsername(username)) {
            throw new CustomServiceException(HttpStatus.CONFLICT, "Username already exists");
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
                .orElseThrow(() -> new CustomServiceException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomServiceException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        return jwtService.generateToken(
                user.getId(),
                user.getUsername());
    }

    public void changePassword(
            Integer userId,
            String currentPassword,
            String newPassword) {

        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomServiceException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"));

        if (!passwordEncoder.matches(
                currentPassword,
                user.getPassword())) {

            throw new CustomServiceException(
                    HttpStatus.UNAUTHORIZED,
                    "Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
    }
}
