package com.connector.gitcon.controller;

import com.connector.gitcon.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    @GetMapping("/api/github/repos")
    public String getRepos() {
        return githubService.fetchRepositories();
    }
}