package com.connector.gitcon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GithubCredentialRequest {

    @Schema(description = "GitHub username", example = "octocat")
    private String githubUsername;

    @Schema(description = "GitHub personal access token", example = "ghp_1234567890abcdefghijklmnopqrstuvwxyz")
    private String accessToken;
}
