package com.example.social.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="posts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Post {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="author_id", nullable=false)
    private Long authorId;

    @Column(name="image_path", nullable=false, length=255)
    private String imagePath;

    @Column(columnDefinition="text")
    private String description;

    @Column(name="like_count", nullable=false)
    private int likeCount;

    @Column(name="view_count", nullable=false)
    private int viewCount;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;

    @Column(nullable=false)
    private boolean deleted;
}