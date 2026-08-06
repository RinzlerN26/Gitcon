package com.connector.gitcon.scanner;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.connector.gitcon.enums.ScannerType;

@Component
@RequiredArgsConstructor
public class SecurityScannerFactory {

        private final SecretsScanner secretsScanner;

        public SecurityScanner getScanner(ScannerType type) {

                switch (type) {
                        case SECRETS:
                                return secretsScanner;

                        default:
                                throw new IllegalArgumentException("Unsupported scanner: " + type);
                }
        }
}