package com.example.hhblogdevelop.dto;

import com.example.hhblogdevelop.entity.Comment;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.entity.Users;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private Users user;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<Comment> comments;
    private int like;


    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = post.getUser();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        this.comments = post.getComments();
        this.like = post.getPost_like();
    }
}