package com.connector.gitcon.scanner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.connector.gitcon.enums.ScannerType;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SecurityScannerFactory {

        private final List<SecurityScanner> scanners;

        public SecurityScanner getScanner(ScannerType scannerType) {

                return scanners.stream()
                                .filter(scanner -> scanner.getScannerType() == scannerType)
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "No scanner found for type: " + scannerType));
        }
}