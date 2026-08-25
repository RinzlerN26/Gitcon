package com.connector.gitcon.controller;

import com.connector.gitcon.dto.request.GithubCredentialRequest;
import com.connector.gitcon.dto.response.GithubCredentialResponse;
import com.connector.gitcon.service.GithubCredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github/credentials")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "GitHub Credentials", description = "Manage GitHub credentials for the authenticated user")
public class GithubCredentialController {

        private final GithubCredentialService githubCredentialService;

        @Operation(summary = "Save GitHub credential", description = "Stores the authenticated user's GitHub credential securely")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "GitHub credential saved successfully"),
                        @ApiResponse(responseCode = "400", description = "GitHub credential already exists"),
                        @ApiResponse(responseCode = "401", description = "Authentication required")
        })
        @PostMapping
        public ResponseEntity<GithubCredentialResponse> saveCredential(
                        @RequestBody GithubCredentialRequest request,
                        Authentication authentication) {

                Integer userId = (Integer) authentication.getPrincipal();

                GithubCredentialResponse response = githubCredentialService.saveCredential(
                                userId,
                                request);

                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Get GitHub credential", description = "Returns GitHub account information for the authenticated user. The access token is never returned.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "GitHub credential found"),
                        @ApiResponse(responseCode = "401", description = "Authentication required"),
                        @ApiResponse(responseCode = "404", description = "GitHub credential not found")
        })
        @GetMapping
        public ResponseEntity<GithubCredentialResponse> getCredential(
                        Authentication authentication) {

                Integer userId = (Integer) authentication.getPrincipal();

                GithubCredentialResponse response = githubCredentialService.getCredential(
                                userId);

                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Delete GitHub credential", description = "Deletes the authenticated user's stored GitHub credential")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "GitHub credential deleted successfully"),
                        @ApiResponse(responseCode = "401", description = "Authentication required"),
                        @ApiResponse(responseCode = "404", description = "GitHub credential not found")
        })
        @DeleteMapping
        public ResponseEntity<Void> deleteCredential(
                        Authentication authentication) {

                Integer userId = (Integer) authentication.getPrincipal();

                githubCredentialService.deleteCredential(
                                userId);

                return ResponseEntity.noContent().build();
        }
}