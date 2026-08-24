package com.connector.gitcon.service;

import com.connector.gitcon.dto.response.ScanHistoryResponse;
import com.connector.gitcon.entity.ScanFinding;
import com.connector.gitcon.entity.SecurityScan;
import com.connector.gitcon.entity.User;
import com.connector.gitcon.enums.ScanStatus;
import com.connector.gitcon.enums.ScannerType;
import com.connector.gitcon.repository.SecurityScanRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScanHistoryService {

    private final SecurityScanRepository securityScanRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String startScan(
            ScannerType scanType,
            String owner,
            String repository,
            String commitHash,
            User user) {

        String scanId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        SecurityScan scan = SecurityScan.builder()
                .scanId(scanId)
                .user(user)
                .scanType(scanType)
                .owner(owner)
                .repository(repository)
                .commitHash(commitHash)
                .status(ScanStatus.RUNNING)
                .startedAt(now)
                .totalFindings(0)
                .createdAt(now)
                .build();

        securityScanRepository.save(scan);

        return scanId;
    }

    @Transactional
    public void completeScan(
            String scanId,
            List<ScanFinding> findings) {

        SecurityScan scan = securityScanRepository
                .findByScanId(scanId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Scan not found: " + scanId));

        for (ScanFinding finding : findings) {
            finding.setScan(scan);
            scan.getFindings().add(finding);
        }

        scan.setStatus(ScanStatus.COMPLETED);
        scan.setCompletedAt(LocalDateTime.now());
        scan.setTotalFindings(findings.size());

        securityScanRepository.save(scan);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failScan(String scanId) {

        securityScanRepository
                .findByScanId(scanId)
                .ifPresent(scan -> {
                    scan.setStatus(ScanStatus.FAILED);
                    scan.setCompletedAt(LocalDateTime.now());

                    securityScanRepository.save(scan);
                });
    }

    @Transactional(readOnly = true)
    public List<ScanHistoryResponse> getUserScanHistory(Integer userId) {

        return securityScanRepository
                .findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(scan -> ScanHistoryResponse.builder()
                        .scanId(scan.getScanId())
                        .scanType(scan.getScanType().name())
                        .owner(scan.getOwner())
                        .repository(scan.getRepository())
                        .commitHash(scan.getCommitHash())
                        .status(scan.getStatus().name())
                        .totalFindings(scan.getTotalFindings())
                        .startedAt(scan.getStartedAt())
                        .completedAt(scan.getCompletedAt())
                        .createdAt(scan.getCreatedAt())
                        .build())
                .toList();
    }
}