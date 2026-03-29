package com.connector.gitcon.dto.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class RepoResponse {

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("html_url")
    private String htmlUrl;
}