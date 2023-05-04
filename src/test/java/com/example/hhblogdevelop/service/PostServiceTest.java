package com.example.hhblogdevelop.service;

import com.example.hhblogdevelop.dto.PostRequestDto;
import com.example.hhblogdevelop.entity.Post;
import com.example.hhblogdevelop.entity.UserRoleEnum;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.exception.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PostServiceTest {

    @Test
    @DisplayName("게시물 작성 Test")
    void createPost() {
        // Given - 준비
        Users user = new Users("dasom12", "pwd1234!!", UserRoleEnum.USER);
        String title = "";
        String content = "글의 내용입니다.";

        PostRequestDto requestDto = new PostRequestDto(title, content);

        // When - 테스트 로직 수행
        Exception exception = assertThrows(CustomException.class, () ->
                new Post(requestDto, user)
        );
        // Then - 검증


    }
    @Test
    @DisplayName("게시물 전체 조회 Test")
    void getAllPosts() {

    }


    @Test
    void searchPost() {
    }

    @Test
    void getPost() {
    }

    @Test
    void updatePost() {
    }

    @Test
    void deletePost() {
    }

    @Test
    void updateLike() {
    }
}