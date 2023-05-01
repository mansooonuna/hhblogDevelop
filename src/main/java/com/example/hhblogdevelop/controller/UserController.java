package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.UserRequestDto;
import com.example.hhblogdevelop.dto.SignupRequestDto;
import com.example.hhblogdevelop.security.UserDetailsImpl;
import com.example.hhblogdevelop.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Tag(name = "User Controller", description = "회원 관련 api")
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
    public GlobalResponseDto login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(userRequestDto, httpServletResponse);
    }

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public GlobalResponseDto withdraw(@RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.withdraw(userRequestDto, userDetails.getUser());
    }


}

