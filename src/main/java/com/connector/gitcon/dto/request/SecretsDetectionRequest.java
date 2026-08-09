package com.connector.gitcon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.connector.gitcon.enums.ScannerType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    private ScannerType scanType;
}
