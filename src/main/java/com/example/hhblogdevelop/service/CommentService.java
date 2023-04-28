package com.example.hhblogdevelop.service;

import com.example.hhblogdevelop.dto.CommentRequestDto;
import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.entity.Comment;
import com.example.hhblogdevelop.entity.CommentLike;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.exception.CustomException;
import com.example.hhblogdevelop.repository.CommentLikeRepository;
import com.example.hhblogdevelop.repository.CommentRepository;
import com.example.hhblogdevelop.repository.PostRepository;
import com.example.hhblogdevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.hhblogdevelop.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 댓글 등록
    @Transactional
    public GlobalResponseDto addComment(CommentRequestDto commentRequestDto, Users user) {
        Post post = postRepository.findById(commentRequestDto.getPostId()).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND)
        );

        Comment comment = new Comment(user, commentRequestDto, post);
        commentRepository.saveAndFlush(comment);
        return new GlobalResponseDto("댓글이 등록되었습니다.", HttpStatus.OK.value());
    }

    // 댓글 수정
    @Transactional
    public GlobalResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, Users user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        if (comment.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            comment.update(commentRequestDto);
            return new GlobalResponseDto("댓글이 수정되었습니다.", HttpStatus.OK.value());
        } else {
            throw new CustomException(INVALID_USER);
        }

    }

    // 댓글 삭제
    @Transactional
    public GlobalResponseDto deleteComment(Long id, Users user) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        if (comment.getUsers().getUsername().equals(user.getUsername()) || user.getRole().equals(user.getRole().ADMIN)) {
            commentRepository.delete(comment);
            return new GlobalResponseDto("댓글이 삭제되었습니다.", HttpStatus.OK.value());
        } else {
            throw new CustomException(INVALID_USER);
        }

    }

    // 댓글 좋아요
    @Transactional
    public GlobalResponseDto likeComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(COMMENT_NOT_FOUND)
        );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new CustomException(INVALID_USER)
        );

        if (commentLikeRepository.findByCommentAndUser(comment, user) == null) {
            commentLikeRepository.save(new CommentLike(comment, user));
            comment.updateLike(true);
            return new GlobalResponseDto("댓글 좋아요", HttpStatus.OK.value());
        } else {
            CommentLike commentLike = commentLikeRepository.findByCommentAndUser(comment, user);
            commentLikeRepository.delete(commentLike);
            comment.updateLike(false);
            return new GlobalResponseDto("댓글 싫어요", HttpStatus.OK.value());
        }
    }

}
