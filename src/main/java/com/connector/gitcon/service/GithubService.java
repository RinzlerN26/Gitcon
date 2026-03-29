package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.RepoResponse;

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
}