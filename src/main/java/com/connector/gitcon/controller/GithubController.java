package com.connector.gitcon.controller;

import com.connector.gitcon.dto.response.RepoResponse;
import com.connector.gitcon.service.GithubService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/api/github/repos")
    public List<RepoResponse> getRepos() {
        return githubService.fetchRepositories();
    }
}