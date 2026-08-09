package com.connector.gitcon.repository;

import com.connector.gitcon.entity.ScanFinding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScanFindingRepository
        extends JpaRepository<ScanFinding, Long> {

    List<ScanFinding> findByScanId(Long scanId);
}