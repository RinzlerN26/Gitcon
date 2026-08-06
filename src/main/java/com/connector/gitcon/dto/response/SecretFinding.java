package com.connector.gitcon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretFinding {
    private String fileName;
    private String secretType;
    private String severity;
    private String description;
    private String lineContext;
    private int lineNumber;
    private String confidence;
}
