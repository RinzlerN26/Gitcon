package com.connector.gitcon.client;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiClient {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.model:gemini-2.5-flash}")
    private String model;

    private Client client;

    private void initializeClient() {
        if (client == null) {
            client = Client.builder()
                    .apiKey(apiKey)
                    .build();
        }
    }

    public String analyzeForSecrets(String commitContent) {

        try {

            log.info("Starting secrets analysis with Gemini API");

            initializeClient();

            String prompt = buildSecretsDetectionPrompt(commitContent);

            GenerateContentResponse response = client.models.generateContent(
                    model,
                    prompt,
                    null);

            String result = response.text();

            log.info("Secrets analysis completed successfully");

            return result;

        } catch (Exception e) {

            log.error("Error analyzing commit for secrets using Gemini", e);

            throw new RuntimeException(
                    "Failed to analyze commit for secrets",
                    e);
        }
    }

    private String buildSecretsDetectionPrompt(String commitContent) {

        return """
                You are a cybersecurity expert.

                Analyze the following code/commit for:
                - API keys
                - Tokens
                - Passwords
                - Private keys
                - Hardcoded credentials
                - Connection strings
                - Sensitive secrets

                Code:
                ```
                """
                + commitContent +
                """
                        ```

                        Return ONLY valid JSON.

                        Expected JSON format:

                        {
                          "secretsFound": true,
                          "riskLevel": "HIGH",
                          "findings": [
                            {
                              "secretType": "API_KEY",
                              "severity": "HIGH",
                              "description": "Hardcoded GitHub token detected",
                              "lineContext": "String token = ghp_xxxxx",
                              "lineNumber": 12
                            }
                          ]
                        }
                        """;
    }
}