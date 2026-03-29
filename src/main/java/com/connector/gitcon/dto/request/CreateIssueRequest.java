package com.connector.gitcon.dto.request;

import lombok.Data;

@Data
public class CreateIssueRequest {

    private String owner;
    private String repo;
    private String title;
    private String body;
}