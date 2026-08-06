package com.connector.gitcon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScanSummary {

    private boolean secretsFound;

    private String riskLevel;

    private List<SecretFinding> findings;
}
