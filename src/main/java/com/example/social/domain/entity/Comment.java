package com.example.social.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name="comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name="post_id", nullable=false)
    private Long postId;

    @Column(name="author_id", nullable=false)
    private Long authorId;

    @Column(nullable=false, columnDefinition="text")
    private String content;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Column(nullable=false)
    private boolean deleted;
}