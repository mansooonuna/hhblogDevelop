package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.CommentRequestDto;
import com.example.hhblogdevelop.dto.UserResponseDto;
import com.example.hhblogdevelop.entity.Comment;

import com.example.hhblogdevelop.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment")
    public UserResponseDto<Comment> addComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.addComment(commentRequestDto, userDetails.getUser());
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    public UserResponseDto<Comment> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(id, commentRequestDto, userDetails.getUser());
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")
    public UserResponseDto<Comment> deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(id, userDetails.getUser());
    }

    // 댓글 좋아요
    @PutMapping("/comment/like/{id}")
    public UserResponseDto<Comment> likeComment(@PathVariable Long id) {
        return commentService.likeComment(id);
    }

}
