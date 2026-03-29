package com.connector.gitcon.controller;

import com.connector.gitcon.dto.request.CreateIssueRequest;
import com.connector.gitcon.dto.response.IssueResponse;
import com.connector.gitcon.dto.response.RepoResponse;
import com.connector.gitcon.dto.response.CommitResponse;
import com.connector.gitcon.dto.request.CreatePrRequest;
import com.connector.gitcon.dto.response.PrResponse;
import com.connector.gitcon.service.GithubService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/api/github/repos")
    public List<RepoResponse> getRepos() {
        return githubService.fetchRepositories();
    }

    @PostMapping("/api/github/issues")
    public IssueResponse createIssue(@RequestBody CreateIssueRequest request) {
        return githubService.createIssue(request);
    }

    @GetMapping("/api/github/{owner}/{repo}/issues")
    public ResponseEntity<List<IssueResponse>> getIssues(
            @PathVariable String owner,
            @PathVariable String repo) {
        return ResponseEntity.ok(githubService.getRepoIssues(owner, repo));
    }

    @GetMapping("/api/github/{owner}/{repo}/commits")
    public ResponseEntity<List<CommitResponse>> getCommits(
            @PathVariable String owner,
            @PathVariable String repo) {
        return ResponseEntity.ok(githubService.getRepoCommits(owner, repo));
    }

    @PostMapping("/api/github/{owner}/{repo}/pulls")
    public ResponseEntity<PrResponse> createPullRequest(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestBody CreatePrRequest request) {
        return ResponseEntity.ok(
                githubService.createPullRequest(owner, repo, request));
    }
}