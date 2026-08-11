package com.connector.gitcon.dto.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
