package com.example.hhblogdevelop.repository;

import com.example.hhblogdevelop.entity.Comment;


import com.example.hhblogdevelop.entity.CommentLike;
import com.example.hhblogdevelop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    CommentLike findByCommentIdAndUser(Long commentId, Users user);
}