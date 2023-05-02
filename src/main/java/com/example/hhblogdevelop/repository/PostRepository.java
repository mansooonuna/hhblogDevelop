package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword% OR p.user.username LIKE %:keyword%")
    List<Post> findAllSearch(@Param("keyword") String keyword);
}
