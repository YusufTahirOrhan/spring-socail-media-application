package com.example.social.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name="likes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(Like.PK.class)
public class Like {
    @Id @Column(name="user_id") private Long userId;
    @Id @Column(name="post_id") private Long postId;

    @Column(name="created_at", nullable=false)
    private Instant createdAt;

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PK implements Serializable {
        private Long userId; private Long postId;
    }
}