package com.example.social.domain.repository;

import com.example.social.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndDeletedFalse(Long id);
    List<Post> findAllByDeletedFalseOrderByCreatedAtDesc();
}