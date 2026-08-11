package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.request.SecretsDetectionRequest;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.ScanSummary;
import com.connector.gitcon.dto.response.ScannableContent;
import com.connector.gitcon.dto.response.SecretFinding;
import com.connector.gitcon.dto.response.SecretsDetectionResponse;
import com.connector.gitcon.entity.ScanFinding;
import com.connector.gitcon.scanner.SecurityScanner;
import com.connector.gitcon.scanner.SecurityScannerFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretsDetectionService {

    private final SecurityScannerFactory scannerFactory;
    private final ScanHistoryService scanHistoryService;
    private final GithubCredentialService githubCredentialService;
    private final GithubClient githubClient;

    private final CommitContentService commitContentService;

    public SecretsDetectionResponse scanForSecrets(SecretsDetectionRequest request, Integer userId) {
        String scanId = scanHistoryService.startScan(
                request.getScanType(),
                request.getOwner(),
                request.getRepository(),
                request.getCommitHash());

        try {
            log.info("Starting secrets scan for commit: {}", request.getCommitHash());
            String token = githubCredentialService.getDecryptedToken(userId);
            GithubCommitResponse commit = githubClient.getCommitDetails(
                    request.getOwner(),
                    request.getRepository(),
                    request.getCommitHash(), token);

            List<SecretFinding> allFindings = new ArrayList<>();

            List<String> riskLevels = new ArrayList<>();

            SecurityScanner scanner = scannerFactory.getScanner(request.getScanType());

            if (commit.getFiles() == null || commit.getFiles().isEmpty()) {
                scanHistoryService.completeScan(
                        scanId,
                        new ArrayList<>());

                return SecretsDetectionResponse.builder()
                        .author(commit.getCommit().getAuthor().getName())
                        .secretsFound(false)
                        .findings(new ArrayList<>())
                        .riskLevel("LOW")
                        .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                        .scanId(scanId)
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
            String overallRisk = calculateOverallRisk(riskLevels);

            SecretsDetectionResponse response = SecretsDetectionResponse.builder()
                    .fileName("")
                    .author(
                            commit.getCommit()
                                    .getAuthor()
                                    .getName())
                    .secretsFound(!allFindings.isEmpty())
                    .findings(allFindings)
                    .riskLevel(overallRisk)
                    .timestamp(
                            LocalDateTime.now()
                                    .format(
                                            DateTimeFormatter.ISO_DATE_TIME))
                    .scanId(scanId)
                    .build();

            List<ScanFinding> findings = allFindings.stream()
                    .map(this::mapSecretFinding)
                    .toList();

            scanHistoryService.completeScan(
                    scanId,
                    findings);

            return response;
        } catch (Exception e) {
            log.error("Error scanning for secrets", e);
            scanHistoryService.failScan(scanId);

            return buildErrorResponse(
                    request,
                    scanId,
                    e.getMessage());
        }
    }

    private SecretsDetectionResponse buildErrorResponse(SecretsDetectionRequest request, String scanId,
            String errorMessage) {
        return SecretsDetectionResponse.builder()
                .fileName("")
                .author("")
                .secretsFound(false)
                .findings(new ArrayList<>())
                .riskLevel("UNKNOWN")
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .scanId(scanId)
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

    private ScanFinding mapSecretFinding(
            SecretFinding finding) {

        Map<String, Object> metadata = new HashMap<>();

        metadata.put("secretType", finding.getSecretType());

        return ScanFinding.builder()
                .findingType("SECRET")
                .severity(finding.getSeverity())
                .title(finding.getSecretType())
                .description(finding.getDescription())
                .fileName(finding.getFileName())
                .lineNumber(finding.getLineNumber())
                .lineContext(finding.getLineContext())
                .confidence(finding.getConfidence())
                .metadata(metadata)
                .build();
    }
}
