package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.dto.PostResponseDto;
import com.example.hhblogdevelop.security.UserDetailsImpl;
import com.example.hhblogdevelop.service.PostService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = {"Post Controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    // PostService 연결
    private final PostService postService;

    // 게시물 목록 조회
    @GetMapping("/posts")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    // 게시물 상세 조회
    @GetMapping("/posts/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    // 게시물 등록
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    // 게시물 수정
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getUser());
    }

    // 게시물 삭제
    @DeleteMapping("/post/{id}")
    public GlobalResponseDto deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getUser());
    }

    // 게시물 좋아요
    @PutMapping("/post/like/{id}")
    public GlobalResponseDto updateLike(@PathVariable Long id) {
        return postService.updateLike(id);
    }

}
