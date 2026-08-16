package com.connector.gitcon.controller;

import com.connector.gitcon.dto.response.AuthResponse;
import com.connector.gitcon.dto.request.LoginRequest;
import com.connector.gitcon.dto.request.RegisterRequest;
import com.connector.gitcon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and authentication")
public class AuthController {

        private final AuthService authService;

        @PostMapping("/register")
        @Operation(summary = "Register a new user", description = "Creates a new Gitcon user and returns a JWT token")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "409", description = "Username already exists", content = @Content)
        })
        public ResponseEntity<AuthResponse> register(
                        @RequestBody RegisterRequest request) {

                String token = authService.register(
                                request.getUsername(),
                                request.getPassword());

                return ResponseEntity.ok(
                                new AuthResponse(token));
        }

        @PostMapping("/login")
        @Operation(summary = "Login", description = "Authenticates an existing user and returns a JWT token")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))),
                        @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content)
        })
        public ResponseEntity<AuthResponse> login(
                        @RequestBody LoginRequest request) {

                String token = authService.login(
                                request.getUsername(),
                                request.getPassword());

                return ResponseEntity.ok(
                                new AuthResponse(token));
        }
}