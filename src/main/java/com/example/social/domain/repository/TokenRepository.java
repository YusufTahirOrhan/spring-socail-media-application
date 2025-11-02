package com.example.social.domain.repository;

import com.example.social.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenHashAndRevokedAtIsNull(String tokenHash);

    List<Token> findAllByUser_IdAndRevokedAtIsNull(Long userId);
}
