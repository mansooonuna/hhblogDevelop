package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    void deleteUsersByUsername(String username);
    void deleteRefreshTokenByUsername(String username);

}
