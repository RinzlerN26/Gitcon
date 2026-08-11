package com.connector.gitcon.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;
}