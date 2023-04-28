package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.LoginRequestDto;
import com.example.hhblogdevelop.dto.SignupRequestDto;
import com.example.hhblogdevelop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    // UserService 연결
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public GlobalResponseDto signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    // 로그인
    @PostMapping("/login")
    public GlobalResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(loginRequestDto, httpServletResponse);
    }

}

