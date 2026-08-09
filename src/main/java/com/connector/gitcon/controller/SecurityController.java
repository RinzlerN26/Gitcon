package com.connector.gitcon.controller;

import com.connector.gitcon.dto.request.SecretsDetectionRequest;
import com.connector.gitcon.dto.response.SecretsDetectionResponse;
import com.connector.gitcon.service.SecretsDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
@Tag(name = "Security", description = "Security scanning APIs")
public class SecurityController {

    private final SecretsDetectionService secretsDetectionService;

    @PostMapping("/scan-secrets")
    @Operation(summary = "Scan code for secrets", description = "Scans source code for exposed API keys, passwords, tokens and other sensitive credentials.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Scan completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SecretsDetectionResponse> scanForSecrets(
            @Valid @RequestBody SecretsDetectionRequest request) {
        log.info(
                "Received {} scan request for {}/{} commit {}",
                request.getScanType(),
                request.getOwner(),
                request.getRepository(),
                request.getCommitHash());

        SecretsDetectionResponse response = secretsDetectionService.scanForSecrets(request);

        if (response.isSecretsFound()) {
            log.warn("Secrets found in file");
        } else {
            log.info("No secrets detected");
        }

        return ResponseEntity.ok(response);
    }
}
