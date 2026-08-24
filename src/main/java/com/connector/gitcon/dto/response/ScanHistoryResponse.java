package com.connector.gitcon.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanHistoryResponse {

    private String scanId;

    private String scanType;

    private String owner;

    private String repository;

    private String commitHash;

    private String status;

    private int totalFindings;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private LocalDateTime createdAt;
}