package com.connector.gitcon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(description = "Registered username", example = "nishant")
    private String username;

    @Schema(description = "User password", example = "StrongPassword123")
    private String password;
}
