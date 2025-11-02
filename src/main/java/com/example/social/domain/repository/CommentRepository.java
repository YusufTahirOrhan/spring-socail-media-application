package com.example.social.domain.repository;

import com.example.social.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdAndDeletedFalseOrderByCreatedAtAsc(Long postId);
    Optional<Comment> findByIdAndDeletedFalse(Long id);
}