package com.connector.gitcon.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FileTypeFilterService {
    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            "java",
            "kt",
            "groovy",
            "xml",
            "properties",
            "yml",
            "yaml",
            "json",
            "js",
            "ts",
            "jsx",
            "tsx",
            "go",
            "py",
            "rb",
            "php",
            "cs",
            "cpp",
            "c",
            "h",
            "hpp",
            "sql",
            "sh",
            "env",
            "dockerfile",
            "gradle",
            "md");

    public boolean isScannable(String filename) {

        if (filename == null || filename.isBlank()) {
            return false;
        }

        String lower = filename.toLowerCase();

        if (lower.endsWith("dockerfile")) {
            return true;
        }

        int index = lower.lastIndexOf('.');

        if (index == -1) {
            return false;
        }

        String extension = lower.substring(index + 1);

        return SUPPORTED_EXTENSIONS.contains(extension);
    }
}
