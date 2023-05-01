package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.dto.PostResponseDto;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.security.UserDetailsImpl;
import com.example.hhblogdevelop.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post Controller", description = "게시물 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    // PostService 연결
    private final PostService postService;

    // 게시물 목록 조회
    @GetMapping("/posts")
    public Page<Post> getAllPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc
    ) {
        return postService.getAllPosts(page - 1, size, sortBy, isAsc);
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
    public GlobalResponseDto updateLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updateLike(id, userDetails.getUser());
    }

}
