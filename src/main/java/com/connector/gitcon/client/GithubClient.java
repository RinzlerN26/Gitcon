package com.connector.gitcon.client;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.RepoResponse;
import com.connector.gitcon.exception.GithubApiException;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final WebClient webClient;

    public List<RepoResponse> getUserRepositories() {
        return webClient
                .get()
                .uri("/user/repos")
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new GithubApiException("GitHub API Error: " + body)))
                .bodyToFlux(RepoResponse.class)
                .collectList()
                .block();
    }

    public IssueResponse createIssue(String owner, String repo, CreateIssueRequest request) {

        return webClient
                .post()
                .uri("/repos/{owner}/{repo}/issues", owner, repo)
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .map(body -> new GithubApiException("GitHub API Error: " + body)))
                .bodyToMono(IssueResponse.class)
                .block();
    }
}