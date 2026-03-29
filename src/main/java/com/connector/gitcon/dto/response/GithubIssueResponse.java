package com.connector.gitcon.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GithubIssueResponse {
    private Long id;
    private String title;
    private String state;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("pull_request")
    private Object pullRequest;

}
