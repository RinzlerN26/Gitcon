package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.ScannableContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommitContentService {

    @SuppressWarnings("unused")
    private final GithubClient githubClient;

    public ScannableContent getScannableContent(
            GithubCommitResponse.ChangedFile file) {

        if (file.getPatch() != null && !file.getPatch().isBlank()) {
            return extractPatch(file);
        }

        return downloadCompleteFile(file);
    }

    private ScannableContent downloadCompleteFile(
            GithubCommitResponse.ChangedFile file) {

        throw new UnsupportedOperationException(
                "Large diff fallback not implemented yet");
    }

    private ScannableContent extractPatch(
            GithubCommitResponse.ChangedFile file) {

        StringBuilder builder = new StringBuilder();

        String[] lines = file.getPatch().split("\n");

        for (String line : lines) {

            if (line.startsWith("@@")
                    || line.startsWith("+++")
                    || line.startsWith("---")) {
                continue;
            }

            if (line.startsWith("-")) {
                continue;
            }

            if (line.startsWith(" ")) {
                continue;
            }

            if (line.startsWith("+")) {

                builder.append(line.substring(1))
                        .append("\n");
            }
        }

        return ScannableContent.builder()
                .fileName(file.getFilename())
                .content(builder.toString().trim())
                .patchBased(true)
                .fallbackUsed(false)
                .build();
    }
}
