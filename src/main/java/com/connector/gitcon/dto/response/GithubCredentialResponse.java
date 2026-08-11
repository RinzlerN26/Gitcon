package com.connector.gitcon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GithubCredentialResponse {

    @Schema(description = "GitHub username", example = "octocat")
    private String githubUsername;
}
