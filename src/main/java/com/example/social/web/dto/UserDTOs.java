package com.example.social.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTOs {
    public record UserResponse(Long id,
                               String username,
                               String role,
                               String createdAt
    ){}

    public record UpdatePasswordRequest(
            @NotBlank String currentPassword,
            @NotBlank @Size(min = 8, max = 72) String newPassword
    ){}
}
