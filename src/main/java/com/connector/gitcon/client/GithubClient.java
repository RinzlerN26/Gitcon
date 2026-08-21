package com.connector.gitcon.client;

import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.request.CreatePrRequest;
import com.connector.gitcon.dto.response.CommitResponse;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.GithubFileContentResponse;
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

        public List<RepoResponse> getUserRepositories(String token) {
                return webClient
                                .get()
                                .uri("/user/repos")
                                .headers(headers -> headers.setBearerAuth(token))
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

        public IssueResponse createIssue(String owner, String repo, CreateIssueRequest request, String token) {

                return webClient
                                .post()
                                .uri("/repos/{owner}/{repo}/issues", owner, repo)
                                .headers(headers -> headers.setBearerAuth(token))
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

        public List<IssueResponse> getRepoIssues(String owner, String repo, String token) {
                return webClient.get()
                                .uri("/repos/{owner}/{repo}/issues", owner, repo)
                                .headers(headers -> headers.setBearerAuth(token))
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

        public List<CommitResponse> getRepoCommits(String owner, String repo, String token) {
                return webClient.get()
                                .uri(uriBuilder -> uriBuilder
                                                .path("/repos/{owner}/{repo}/commits")
                                                .queryParam("per_page", 100)
                                                .build(owner, repo))
                                .headers(headers -> headers.setBearerAuth(token))
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

        public PrResponse createPullRequest(String owner, String repo, CreatePrRequest request, String token) {
                return webClient.post()
                                .uri("/repos/{owner}/{repo}/pulls", owner, repo)
                                .headers(headers -> headers.setBearerAuth(token))
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
                        String sha,
                        String token) {

                return webClient
                                .get()
                                .uri("/repos/{owner}/{repo}/commits/{sha}", owner, repo, sha)
                                .headers(headers -> headers.setBearerAuth(token))
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                response -> response.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToMono(GithubCommitResponse.class)
                                .block();
        }

        public String downloadFile(String contentsUrl, String token) {

                GithubFileContentResponse response = webClient
                                .get()
                                .uri(contentsUrl.replace("https://api.github.com", ""))
                                .headers(headers -> headers.setBearerAuth(token))
                                .retrieve()
                                .onStatus(
                                                status -> status.isError(),
                                                clientResponse -> clientResponse.bodyToMono(String.class)
                                                                .map(body -> new GithubApiException(
                                                                                "GitHub API Error: " + body)))
                                .bodyToMono(GithubFileContentResponse.class)
                                .block();

                if (response == null || response.getContent() == null) {
                        return "";
                }

                byte[] decoded = Base64.getDecoder()
                                .decode(response.getContent().replace("\n", ""));

                return new String(decoded);
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