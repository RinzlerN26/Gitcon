package com.connector.gitcon.repository;

import com.connector.gitcon.entity.SecurityScan;
import com.connector.gitcon.entity.User;
import com.connector.gitcon.enums.ScannerType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SecurityScanRepository
                extends JpaRepository<SecurityScan, Long> {

        Optional<SecurityScan> findByScanId(String scanId);

        List<SecurityScan> findByOwnerAndRepositoryOrderByCreatedAtDesc(
                        String owner,
                        String repository);

        List<SecurityScan> findByOwnerAndRepositoryAndScanTypeOrderByCreatedAtDesc(
                        String owner,
                        String repository,
                        ScannerType scanType);

        List<SecurityScan> findByUser_IdOrderByCreatedAtDesc(
                        Integer userId);

        long countByUser(User user);
}