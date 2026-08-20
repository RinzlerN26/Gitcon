package com.connector.gitcon.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    @Size(min = 3, max = 15, message = "Password must be between 3 and 15 characters")
    private String currentPassword;
    @NotBlank
    @Size(min = 3, max = 15, message = "Password must be between 3 and 15 characters")
    private String newPassword;
}