package com.connector.gitcon.entity;

import com.connector.gitcon.enums.ScanStatus;
import com.connector.gitcon.enums.ScannerType;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "security_scans", indexes = {
        @Index(name = "idx_scan_scan_id", columnList = "scan_id"),
        @Index(name = "idx_scan_repository", columnList = "owner, repository"),
        @Index(name = "idx_scan_type", columnList = "scan_type"),
        @Index(name = "idx_scan_created_at", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityScan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scan_id", nullable = false, unique = true, length = 36)
    private String scanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "scan_type", nullable = false, length = 50)
    private ScannerType scanType;

    @Column(nullable = false, length = 255)
    private String owner;

    @Column(nullable = false, length = 255)
    private String repository;

    @Column(name = "commit_hash", nullable = false, length = 100)
    private String commitHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ScanStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "total_findings", nullable = false)
    private int totalFindings;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "scan", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScanFinding> findings = new ArrayList<>();
}