package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.request.CreatePrRequest;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.RepoResponse;
import com.connector.gitcon.dto.response.CommitResponse;
import com.connector.gitcon.dto.response.GithubCommitResponse;
import com.connector.gitcon.dto.response.PrResponse;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubClient githubClient;
    private final GithubCredentialService githubCredentialService;

    public List<RepoResponse> fetchRepositories(Integer userId) {
        return githubClient.getUserRepositories(githubCredentialService.getDecryptedToken(userId));
    }

    public IssueResponse createIssue(CreateIssueRequest request, Integer userId) {
        return githubClient.createIssue(
                request.getOwner(),
                request.getRepo(),
                request,
                githubCredentialService.getDecryptedToken(userId));
    }

    public List<IssueResponse> getRepoIssues(String owner, String repo, Integer userId) {
        String token = githubCredentialService.getDecryptedToken(userId);
        return githubClient.getRepoIssues(owner, repo, token);
    }

    public List<CommitResponse> getRepoCommits(String owner, String repo, Integer userId) {
        String token = githubCredentialService.getDecryptedToken(userId);
        return githubClient.getRepoCommits(owner, repo, token);
    }

    public PrResponse createPullRequest(String owner, String repo, CreatePrRequest request, Integer userId) {
        String token = githubCredentialService.getDecryptedToken(userId);
        return githubClient.createPullRequest(owner, repo, request, token);
    }

    public GithubCommitResponse getCommitDetails(
            String owner,
            String repo,
            String sha,
            Integer userId) {

        String token = githubCredentialService.getDecryptedToken(userId);
        return githubClient.getCommitDetails(owner, repo, sha, token);
    }

    public String downloadFile(String contentsUrl, Integer userId) {
        String token = githubCredentialService.getDecryptedToken(userId);
        return githubClient.downloadFile(contentsUrl, token);
    }
}