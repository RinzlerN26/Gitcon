package com.connector.gitcon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(GithubConfig githubConfig) {
        return WebClient.builder()
                .baseUrl(githubConfig.getBaseUrl())
                .defaultHeader("Authorization", "Bearer " + githubConfig.getToken())
                .defaultHeader("Accept", "application/vnd.github+json")
                .build();
    }
}