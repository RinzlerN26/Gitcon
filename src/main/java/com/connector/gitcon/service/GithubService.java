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

    public List<RepoResponse> fetchRepositories() {
        return githubClient.getUserRepositories();
    }

    public IssueResponse createIssue(CreateIssueRequest request) {
        return githubClient.createIssue(
                request.getOwner(),
                request.getRepo(),
                request);
    }

    public List<IssueResponse> getRepoIssues(String owner, String repo) {
        return githubClient.getRepoIssues(owner, repo);
    }

    public List<CommitResponse> getRepoCommits(String owner, String repo) {
        return githubClient.getRepoCommits(owner, repo);
    }

    public PrResponse createPullRequest(String owner, String repo, CreatePrRequest request) {
        return githubClient.createPullRequest(owner, repo, request);
    }

    public GithubCommitResponse getCommitDetails(
            String owner,
            String repo,
            String sha) {

        return githubClient.getCommitDetails(owner, repo, sha);
    }

    public String downloadFile(String contentsUrl) {
        return githubClient.downloadFile(contentsUrl);
    }
}