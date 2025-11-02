package com.example.social.domain.repository;

import com.example.social.domain.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Like.PK> {
    long countByPostId(Long postId);
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    long deleteByUserIdAndPostId(Long userId, Long postId);
}