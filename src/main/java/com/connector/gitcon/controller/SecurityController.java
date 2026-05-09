package com.connector.gitcon.controller;

import com.connector.gitcon.dto.request.SecretsDetectionRequest;
import com.connector.gitcon.dto.response.SecretsDetectionResponse;
import com.connector.gitcon.service.SecretsDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecretsDetectionService secretsDetectionService;

    @PostMapping("/scan-secrets")
    public ResponseEntity<SecretsDetectionResponse> scanForSecrets(
            @Valid @RequestBody SecretsDetectionRequest request) {
        log.info("Received secrets scan request for file: {}", request.getFileName());

        SecretsDetectionResponse response = secretsDetectionService.scanForSecrets(request);

        if (response.isSecretsFound()) {
            log.warn("Secrets found in file: {} - Risk Level: {}",
                    request.getFileName(), response.getRiskLevel());
        } else {
            log.info("No secrets detected in file: {}", request.getFileName());
        }

        return ResponseEntity.ok(response);
    }
}
