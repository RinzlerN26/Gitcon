package com.connector.gitcon.service;

import com.connector.gitcon.client.GeminiClient;
import com.connector.gitcon.dto.request.SecretsDetectionRequest;
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

    public SecretsDetectionResponse scanForSecrets(SecretsDetectionRequest request) {
        try {
            log.info("Starting secrets scan for file: {}", request.getFileName());

            String analysisResult = geminiClient.analyzeForSecrets(request.getCommitContent());

            return parseAndBuildResponse(analysisResult, request);
        } catch (Exception e) {
            log.error("Error scanning for secrets", e);
            return buildErrorResponse(request, e.getMessage());
        }
    }

    private SecretsDetectionResponse parseAndBuildResponse(String analysisResult, SecretsDetectionRequest request) {
        try {
            String cleanedResponse = cleanJsonResponse(analysisResult);

            JsonNode resultNode = objectMapper.readTree(cleanedResponse);

            boolean secretsFound = resultNode.path("secretsFound").asBoolean(false);
            String riskLevel = resultNode.path("riskLevel").asText("LOW");
            List<SecretFinding> findings = parseFinding(resultNode.path("findings"));

            return SecretsDetectionResponse.builder()
                    .fileName(request.getFileName())
                    .author(request.getAuthor())
                    .secretsFound(secretsFound)
                    .findings(findings)
                    .riskLevel(riskLevel)
                    .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .scanId(UUID.randomUUID().toString())
                    .build();
        } catch (Exception e) {
            log.error("Error parsing analysis result", e);
            return buildErrorResponse(request, "Failed to parse analysis result");
        }
    }

    private List<SecretFinding> parseFinding(JsonNode findingsNode) {
        List<SecretFinding> findings = new ArrayList<>();

        if (findingsNode.isArray()) {
            findingsNode.forEach(node -> {
                SecretFinding finding = new SecretFinding(
                        node.path("fileName").asText(),
                        node.path("secretType").asText(),
                        node.path("severity").asText(),
                        node.path("description").asText(),
                        node.path("lineContext").asText(),
                        node.path("lineNumber").asInt(0));
                findings.add(finding);
            });
        }

        return findings;
    }

    private SecretsDetectionResponse buildErrorResponse(SecretsDetectionRequest request, String errorMessage) {
        return SecretsDetectionResponse.builder()
                .fileName(request.getFileName())
                .author(request.getAuthor())
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
