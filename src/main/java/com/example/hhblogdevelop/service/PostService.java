package com.example.hhblogdevelop.service;


import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.dto.PostResponseDto;
import com.example.hhblogdevelop.dto.UserResponseDto;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.entity.PostLike;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.exception.CustomException;
import com.example.hhblogdevelop.exception.ErrorCode;
import com.example.hhblogdevelop.jwt.JwtUtil;
import com.example.hhblogdevelop.repository.CommentRepository;
import com.example.hhblogdevelop.repository.PostLikeRepository;
import com.example.hhblogdevelop.repository.PostRepository;
import com.example.hhblogdevelop.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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
    public UserResponseDto<Post> deletePost(Long id,  Users user) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        if (post.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            postRepository.delete(post);
            return UserResponseDto.setSuccess("게시글 삭제 성공");
        } else {
            throw new CustomException(INVALID_USER);
        }

    }

    // 좋아요
    @Transactional
    public UserResponseDto<Post> updateLike(Long id) {
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
            return UserResponseDto.setSuccess("좋아요 성공");
        } else {
            PostLike postLike = postLikeRepository.findByPostAndUser(post, user);
            postLikeRepository.delete(postLike);
            post.updateLike(false);
            return UserResponseDto.setSuccess("좋아요 취소");
        }

    }
}

