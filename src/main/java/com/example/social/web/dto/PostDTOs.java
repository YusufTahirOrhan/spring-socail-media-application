package com.example.social.web.dto;

import jakarta.validation.constraints.*;

import java.time.Instant;
import java.util.List;

public class PostDTOs {
    public record PostResponse(
            Long id,
            Author author,
            String description,
            String imageUrl,
            int likeCount,
            int viewCount,
            Instant createdAt,
            Instant updatedAt,
            List<CommentResponse> comments
    ) {
        public record Author(Long id, String username) {}
    }

    public record CommentCreateRequest(
            @NotBlank @Size(max=2000) String content
    ) {}

    public record CommentResponse(
            Long id, Long authorId, String content, Instant createdAt
    ) {}
}