package com.connector.gitcon.dto.response;

import lombok.Data;

@Data
public class GithubFileContentResponse {
    private String content;

    private String encoding;
}
