package com.connector.gitcon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "scan_findings", indexes = {
        @Index(name = "idx_finding_scan_id", columnList = "scan_id"),
        @Index(name = "idx_finding_severity", columnList = "severity"),
        @Index(name = "idx_finding_type", columnList = "finding_type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanFinding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scan_id", nullable = false, foreignKey = @ForeignKey(name = "fk_scan_finding_scan"))
    private SecurityScan scan;

    @Column(name = "finding_type", nullable = false, length = 100)
    private String findingType;

    @Column(length = 30)
    private String severity;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "file_name", length = 500)
    private String fileName;

    @Column(name = "line_number")
    private Integer lineNumber;

    @Column(name = "line_context", columnDefinition = "TEXT")
    private String lineContext;

    @Column(length = 30)
    private String confidence;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
}