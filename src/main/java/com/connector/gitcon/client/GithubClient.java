package com.connector.gitcon.client;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.request.CreatePrRequest;
import com.connector.gitcon.dto.response.CommitResponse;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.GithubIssueResponse;
import com.connector.gitcon.dto.response.GithubPullRequestResponse;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.PrResponse;
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
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
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
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToMono(IssueResponse.class)
                                .block();
        }

        public List<IssueResponse> getRepoIssues(String owner, String repo) {
                return webClient.get()
                                .uri("/repos/{owner}/{repo}/issues", owner, repo)
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToFlux(GithubIssueResponse.class)
                                .filter(issue -> issue.getPullRequest() == null)
                                .map(this::mapIssue)
                                .collectList()
                                .block();
        }

        public List<CommitResponse> getRepoCommits(String owner, String repo) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/repos/{owner}/{repo}/commits")
                                                .queryParam("per_page", 100)
                                                .build(owner, repo))
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToFlux(GithubCommitResponse.class)
                                .map(this::mapCommit)
                                .collectList()
                                .block();
        }

        public PrResponse createPullRequest(String owner, String repo, CreatePrRequest request) {
                return webClient.post()
                                .uri("/repos/{owner}/{repo}/pulls", owner, repo)
                                .bodyValue(request)
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToMono(GithubPullRequestResponse.class)
                                .map(this::mapPullRequest)
                                .block();
        }

        public GithubCommitResponse getCommitDetails(
                        String owner,
                        String repo,
                        String sha) {

                return webClient
                                .get()
                                .uri("/repos/{owner}/{repo}/commits/{sha}", owner, repo, sha)
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToMono(GithubCommitResponse.class)
                                .block();
        }

        private IssueResponse mapIssue(GithubIssueResponse res) {
                IssueResponse dto = new IssueResponse();
                dto.setId(res.getId());
                dto.setTitle(res.getTitle());
                dto.setState(res.getState());
                dto.setHtmlUrl(res.getHtmlUrl());
                return dto;
        }

        private CommitResponse mapCommit(GithubCommitResponse res) {
                CommitResponse dto = new CommitResponse();
                dto.setSha(res.getSha());
                dto.setMessage(res.getCommit().getMessage());
                dto.setAuthorName(res.getCommit().getAuthor().getName());
                dto.setHtmlUrl(res.getHtmlUrl());
                return dto;
        }

        private PrResponse mapPullRequest(GithubPullRequestResponse res) {
                PrResponse dto = new PrResponse();
                dto.setId(res.getId());
                dto.setTitle(res.getTitle());
                dto.setState(res.getState());
                dto.setHtmlUrl(res.getHtmlUrl());
                return dto;
        }
}