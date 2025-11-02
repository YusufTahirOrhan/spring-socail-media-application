package com.example.social.security;

import com.example.social.domain.Role;
import lombok.RequiredArgsConstructor;

public record CurrentUser(Long id, String username, Role role) {
}
