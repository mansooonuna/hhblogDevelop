package com.example.hhblogdevelop.service;


import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.dto.PostResponseDto;
import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.entity.PostLike;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.exception.CustomException;
import com.example.hhblogdevelop.repository.PostLikeRepository;
import com.example.hhblogdevelop.repository.PostRepository;
import com.example.hhblogdevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.hhblogdevelop.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class PostService {

    // PostRepository 연결
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    // UserRepository 연결
    private final UserRepository userRepository;

    // 전체 게시물 목록 조회
    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).collect(Collectors.toList());
    }

    // 선택한 게시물 상세 조회
    @Transactional(readOnly = true)
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );
        return new PostResponseDto(post);
    }

    // 게시물 등록
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Users user) {
        Post post = postRepository.saveAndFlush(new Post(postRequestDto, user));
        return new PostResponseDto(post);
    }

    // 게시물 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto postRequestDto, Users user) {

        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        if (post.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            post.update(postRequestDto);
            return new PostResponseDto(post);
        } else {
            throw new CustomException(INVALID_USER);
        }
    }

    // 게시물 삭제
    @Transactional
    public GlobalResponseDto deletePost(Long id, Users user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        if (post.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            postRepository.delete(post);
            return new GlobalResponseDto("게시물이 삭제되었습니다.", HttpStatus.OK.value());
        } else {
            throw new CustomException(INVALID_USER);
        }

    }

    // 좋아요
    @Transactional
    public GlobalResponseDto updateLike(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new CustomException(INVALID_USER)
        );

        if (postLikeRepository.findByPostAndUser(post, user) == null) {
            postLikeRepository.save(new PostLike(post, user));
            post.updateLike(true);
            return new GlobalResponseDto("게시물 좋아요", HttpStatus.OK.value());
        } else {
            PostLike postLike = postLikeRepository.findByPostAndUser(post, user);
            postLikeRepository.delete(postLike);
            post.updateLike(false);
            return new GlobalResponseDto("게시물 싫어요", HttpStatus.OK.value());
        }

    }
}

