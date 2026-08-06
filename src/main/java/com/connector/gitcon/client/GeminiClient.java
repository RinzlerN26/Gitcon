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

    private String buildSecretsDetectionPrompt(String content) {

        return """
                You are a cybersecurity expert specializing in secret detection.

                The following content contains newly added or modified lines from a Git commit.

                Analyze ONLY the provided content for exposed secrets such as:
                - GitHub Personal Access Tokens
                - AWS Access Keys
                - AWS Secret Keys
                - Google API Keys
                - Azure Keys
                - JWT Secrets
                - API Keys
                - Access Tokens
                - Passwords
                - Private Keys
                - Connection Strings
                - Other hardcoded credentials

                Rules:
                - Analyze ONLY the provided content.
                - Do NOT invent findings.
                - Do NOT report placeholder values such as:
                  - YOUR_API_KEY
                  - CHANGE_ME
                  - example
                  - test
                  - dummy
                  - localhost
                - Ignore environment variable references such as:
                  System.getenv(...)
                  process.env.*
                  ${API_KEY}
                - Report a finding only when the value appears to be a real secret.
                - Keep descriptions concise.
                - The lineNumber must refer to the provided snippet.
                - Return ONLY valid JSON.
                - Do not include markdown or code fences.

                Allowed risk levels:
                LOW
                MEDIUM
                HIGH
                CRITICAL

                Allowed secret types:
                GITHUB_TOKEN
                AWS_ACCESS_KEY
                AWS_SECRET_KEY
                GOOGLE_API_KEY
                AZURE_KEY
                JWT_SECRET
                API_KEY
                ACCESS_TOKEN
                PASSWORD
                PRIVATE_KEY
                DATABASE_CONNECTION_STRING
                OTHER_SECRET

                Code:
                ```
                """
                + content +
                """
                        ```

                        Return exactly this JSON structure:

                        {
                          "secretsFound": true,
                          "riskLevel": "HIGH",
                          "findings": [
                            {
                              "secretType": "GITHUB_TOKEN",
                              "severity": "HIGH",
                              "description": "Hardcoded GitHub Personal Access Token detected.",
                              "lineContext": "String token = \\"ghp_xxxxxxxxx\\";",
                              "lineNumber": 4,
                              "confidence": "HIGH"
                            }
                          ]
                        }

                        If no secrets are found, return:

                        {
                          "secretsFound": false,
                          "riskLevel": "LOW",
                          "findings": []
                        }
                        """;
    }
}