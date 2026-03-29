package com.connector.gitcon.dto.response;

import lombok.Data;

@Data
public class GithubCommitResponse {
    private String sha;
    private String html_url;
    private Commit commit;

    public String getHtmlUrl() {
        return html_url;
    }

    @Data
    public static class Commit {
        private Author author;
        private String message;
    }

    @Data
    public static class Author {
        private String name;
    }
}
