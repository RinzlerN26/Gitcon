package com.connector.gitcon.exception;

public class GithubApiException extends RuntimeException {

    public GithubApiException(String message) {
        super(message);
    }
}