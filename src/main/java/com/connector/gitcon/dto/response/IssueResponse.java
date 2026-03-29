package com.connector.gitcon.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueResponse {

    private long id;

    private String title;

    private String body;

    @JsonProperty("html_url")
    private String htmlUrl;

    private String state;
}