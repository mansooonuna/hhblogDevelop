package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.RefreshToken;
import com.example.hhblogdevelop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(Users user);

    Optional<RefreshToken> findUserByRefreshToken(String token);
}