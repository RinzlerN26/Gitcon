package com.connector.gitcon.dto.response;

import lombok.Data;

@Data
public class CommitResponse {
    private String sha;
    private String message;
    private String authorName;
    private String htmlUrl;
}
