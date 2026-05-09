package com.connector.gitcon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretsDetectionRequest {
    @NotBlank(message = "Commit content is required")
    private String commitContent;

    private String fileName;

    private String author;
}
