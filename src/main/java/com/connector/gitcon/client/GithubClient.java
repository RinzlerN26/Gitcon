package com.connector.gitcon.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GithubClient {

    private final WebClient webClient;

    public String getUserRepositories() {
        return webClient
                .get()
                .uri("/user/repos")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}