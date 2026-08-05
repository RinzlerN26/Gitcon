package com.connector.gitcon.service;

import com.connector.gitcon.client.GeminiClient;
import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.request.SecretsDetectionRequest;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.ScannableContent;
import com.connector.gitcon.dto.response.SecretFinding;
import com.connector.gitcon.dto.response.SecretsDetectionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecretsDetectionService {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;
    private final GithubClient githubClient;

    private final CommitContentService commitContentService;

    public SecretsDetectionResponse scanForSecrets(SecretsDetectionRequest request) {
        try {
            log.info("Starting secrets scan for file");

            GithubCommitResponse commit = githubClient.getCommitDetails(
                    request.getOwner(),
                    request.getRepository(),
                    request.getCommitHash());

            List<SecretFinding> findings = new ArrayList<>();

            for (GithubCommitResponse.ChangedFile file : commit.getFiles()) {
                ScannableContent content = commitContentService.getScannableContent(file);
                if (content.isSkipped()) {
                    continue;
                }
                if (content.getContent().isBlank()) {
                    continue;
                }
                String analysisResult = geminiClient.analyzeForSecrets(content.getContent());
                findings.addAll(
                        parseFindings(
                                analysisResult,
                                content.getFileName()));
            }
            return SecretsDetectionResponse.builder()
                    .fileName("")
                    .author(commit.getCommit().getAuthor().getName())
                    .secretsFound(!findings.isEmpty())
                    .findings(findings)
                    .riskLevel(findings.isEmpty() ? "LOW" : "HIGH")
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .scanId(UUID.randomUUID().toString())
                    .build();
        } catch (Exception e) {
            log.error("Error scanning for secrets", e);
            return buildErrorResponse(request, e.getMessage());
        }
    }

    private List<SecretFinding> parseFindings(
            String analysisResult,
            String fileName) {

        List<SecretFinding> findings = new ArrayList<>();

        try {

            String cleanedResponse = cleanJsonResponse(analysisResult);

            JsonNode rootNode = objectMapper.readTree(cleanedResponse);

            JsonNode findingsNode = rootNode.path("findings");

            if (findingsNode.isArray()) {

                findingsNode.forEach(node -> {

                    SecretFinding finding = new SecretFinding(
                            fileName,
                            node.path("secretType").asText(),
                            node.path("severity").asText(),
                            node.path("description").asText(),
                            node.path("lineContext").asText(),
                            node.path("lineNumber").asInt(0));

                    findings.add(finding);
                });
            }

        } catch (Exception e) {
            log.error("Failed to parse Gemini response for file: {}", fileName, e);
        }

        return findings;
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

    private String cleanJsonResponse(String response) {

        return response
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }
}
