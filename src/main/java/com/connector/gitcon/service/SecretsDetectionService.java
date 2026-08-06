package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.request.SecretsDetectionRequest;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.ScanSummary;
import com.connector.gitcon.dto.response.ScannableContent;
import com.connector.gitcon.dto.response.SecretFinding;
import com.connector.gitcon.dto.response.SecretsDetectionResponse;
import com.connector.gitcon.scanner.SecurityScanner;
import com.connector.gitcon.scanner.SecurityScannerFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretsDetectionService {

    private final SecurityScannerFactory scannerFactory;
    private final GithubClient githubClient;

    private final CommitContentService commitContentService;

    public SecretsDetectionResponse scanForSecrets(SecretsDetectionRequest request) {
        try {
            log.info("Starting secrets scan for commit: {}", request.getCommitHash());

            GithubCommitResponse commit = githubClient.getCommitDetails(
                    request.getOwner(),
                    request.getRepository(),
                    request.getCommitHash());

            List<SecretFinding> allFindings = new ArrayList<>();

            List<String> riskLevels = new ArrayList<>();

            SecurityScanner scanner = scannerFactory.getScanner(request.getScanType());

            if (commit.getFiles() == null || commit.getFiles().isEmpty()) {

                return SecretsDetectionResponse.builder()
                        .author(commit.getCommit().getAuthor().getName())
                        .secretsFound(false)
                        .findings(new ArrayList<>())
                        .riskLevel("LOW")
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .scanId(UUID.randomUUID().toString())
                        .build();
            }

            for (GithubCommitResponse.ChangedFile file : commit.getFiles()) {
                ScannableContent content = commitContentService.getScannableContent(file);
                if (content.isSkipped()) {
                    continue;
                }
                if (content.getContent().isBlank()) {
                    continue;
                }
                ScanSummary summary = scanner.scan(content);

                allFindings.addAll(summary.getFindings());

                riskLevels.add(summary.getRiskLevel());
            }
            return SecretsDetectionResponse.builder()
                    .fileName("")
                    .author(commit.getCommit().getAuthor().getName())
                    .secretsFound(!allFindings.isEmpty())
                    .findings(allFindings)
                    .riskLevel(calculateOverallRisk(riskLevels))
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .scanId(UUID.randomUUID().toString())
                    .build();
        } catch (Exception e) {
            log.error("Error scanning for secrets", e);
            return buildErrorResponse(request, e.getMessage());
        }
    }

    private SecretsDetectionResponse buildErrorResponse(SecretsDetectionRequest request, String errorMessage) {
        return SecretsDetectionResponse.builder()
                .fileName("")
                .author("")
                .secretsFound(false)
                .findings(new ArrayList<>())
                .riskLevel("UNKNOWN")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .scanId(UUID.randomUUID().toString())
                .build();
    }

    private String calculateOverallRisk(List<String> riskLevels) {

        if (riskLevels.contains("CRITICAL")) {
            return "CRITICAL";
        }

        if (riskLevels.contains("HIGH")) {
            return "HIGH";
        }

        if (riskLevels.contains("MEDIUM")) {
            return "MEDIUM";
        }

        return "LOW";
    }
}
