package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUsers_Username(String username, Pageable pageable);

}
