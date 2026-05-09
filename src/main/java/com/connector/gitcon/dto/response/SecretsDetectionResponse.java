package com.connector.gitcon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecretsDetectionResponse {
    private String fileName;
    private String author;
    private boolean secretsFound;
    private List<SecretFinding> findings;
    private String riskLevel;
    private String timestamp;
    private String scanId;
}
