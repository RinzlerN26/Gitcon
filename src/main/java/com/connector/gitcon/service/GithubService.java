package com.connector.gitcon.service;

import com.connector.gitcon.client.GithubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubClient githubClient;

    public String fetchRepositories() {
        return githubClient.getUserRepositories();
    }
}