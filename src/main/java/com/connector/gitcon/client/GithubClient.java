package com.connector.gitcon.client;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.connector.gitcon.dto.response.RepoResponse;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final WebClient webClient;

    public List<RepoResponse> getUserRepositories() {
        return webClient
                .get()
                .uri("/user/repos")
                .retrieve()
                .bodyToFlux(RepoResponse.class)
                .collectList()
                .block();
    }
}