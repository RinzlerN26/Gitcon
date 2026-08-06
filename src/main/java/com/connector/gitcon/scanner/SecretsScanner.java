package com.connector.gitcon.scanner;

import com.connector.gitcon.client.GeminiClient;
import com.connector.gitcon.dto.response.ScanSummary;
import com.connector.gitcon.dto.response.ScannableContent;
import com.connector.gitcon.dto.response.SecretFinding;
import com.connector.gitcon.enums.ScannerType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecretsScanner implements SecurityScanner {

    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    @Override
    public ScannerType getScannerType() {
        return ScannerType.SECRETS;
    }

    @Override
    public ScanSummary scan(ScannableContent content) {

        String response = geminiClient.analyzeForSecrets(content.getContent());

        return parseResponse(response, content.getFileName());
    }

    private ScanSummary parseResponse(String analysisResult,
            String fileName) {

        List<SecretFinding> findings = new ArrayList<>();

        boolean secretsFound = false;
        String riskLevel = "LOW";

        try {

            JsonNode root = objectMapper.readTree(
                    analysisResult
                            .replace("```json", "")
                            .replace("```", "")
                            .trim());

            secretsFound = root.path("secretsFound").asBoolean(false);

            riskLevel = root.path("riskLevel").asText("LOW");

            JsonNode array = root.path("findings");

            if (array.isArray()) {

                array.forEach(node -> findings.add(

                        new SecretFinding(
                                fileName,
                                node.path("secretType").asText(),
                                node.path("severity").asText(),
                                node.path("description").asText(),
                                node.path("lineContext").asText(),
                                node.path("lineNumber").asInt(0),
                                node.path("confidence").asText("MEDIUM"))

                ));
            }

        } catch (Exception e) {

            log.error("Failed parsing Gemini response", e);
        }

        return new ScanSummary(
                secretsFound,
                riskLevel,
                findings);
    }
}