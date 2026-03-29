package com.connector.gitcon.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubPullRequestResponse {
    private Long id;
    private String title;
    private String state;
    @JsonProperty("html_url")
    private String htmlUrl;
}
