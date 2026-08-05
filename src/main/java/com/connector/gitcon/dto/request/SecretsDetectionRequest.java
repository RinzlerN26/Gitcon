package com.connector.gitcon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecretsDetectionRequest {
    @NotBlank
    private String owner;

    @NotBlank
    private String repository;

    @NotBlank
    private String commitHash;
}
