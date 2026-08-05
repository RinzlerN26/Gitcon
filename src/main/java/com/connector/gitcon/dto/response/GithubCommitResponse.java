package com.connector.gitcon.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class GithubCommitResponse {
    private String sha;
    private String html_url;
    private Commit commit;

    private List<ChangedFile> files;

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

    @Data
    public static class ChangedFile {

        private String filename;

        private String status;

        private String patch;

        private String raw_url;

        private String blob_url;

        private String contents_url;

        private Integer additions;

        private Integer deletions;

        private Integer changes;
    }
}
