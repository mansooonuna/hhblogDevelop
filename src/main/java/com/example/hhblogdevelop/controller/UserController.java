package com.example.hhblogdevelop.controller;

import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.UserRequestDto;
import com.example.hhblogdevelop.dto.SignupRequestDto;
import com.example.hhblogdevelop.security.UserDetailsImpl;
import com.example.hhblogdevelop.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    // 회원 탈퇴
    @DeleteMapping("/withdraw")
    public GlobalResponseDto withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.withdraw(userDetails.getUser());
    }

    // 로그인
    @PostMapping("/login")
    public GlobalResponseDto login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse httpServletResponse) {
        return userService.login(userRequestDto, httpServletResponse);
    }

    // 로그아웃
    @GetMapping("/logout")
    public GlobalResponseDto logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return userService.logout(userDetails.getUser(), request);
    }


}
