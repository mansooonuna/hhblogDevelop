package com.example.hhblogdevelop.service;


import com.example.hhblogdevelop.dto.GlobalResponseDto;
import com.example.hhblogdevelop.dto.LoginRequestDto;
import com.example.hhblogdevelop.dto.SignupRequestDto;
import com.example.hhblogdevelop.dto.TokenDto;
import com.example.hhblogdevelop.entity.RefreshToken;
import com.example.hhblogdevelop.entity.RoleType;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.exception.CustomException;
import com.example.hhblogdevelop.jwt.JwtUtil;
import com.example.hhblogdevelop.repository.RefreshTokenRepository;
import com.example.hhblogdevelop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.hhblogdevelop.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {

    // UserRepository 연결
    private final UserRepository userRepository;

    // JwtUtil 연결
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    @Transactional
    public GlobalResponseDto signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 회원 중복 확인
        Optional<Users> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            throw new CustomException(INVALID_USER_EXISTENCE);
        }

        // 관리자 확인
        RoleType role = RoleType.USER;
        if (signupRequestDto.isAdmin()) {
            if (!signupRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                throw new CustomException(INVALID_ADMIN_PASSWORD);
            }
            role = RoleType.ADMIN;
        }

        Users users = new Users(username, password, role);
        userRepository.save(users);
        return new GlobalResponseDto("회원가입 완료", HttpStatus.OK.value());
    }

    //로그인
    @Transactional
    public GlobalResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        Users user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        // 비밀번호 확인
        if(!passwordEncoder.matches(password, user.getPassword())){
            return new GlobalResponseDto("비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST.value());
        }

        //아이디 정보로 토큰 생성
        TokenDto tokenDto = jwtUtil.createAllToken(username, user.getRole());

        //Refresh토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUsername(username);

        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), username);
            refreshTokenRepository.save(newToken);
        }
        //response 헤더에 AccessToken / RefreshToken
        setHeader(response, tokenDto);
        return new GlobalResponseDto("정상적으로 로그인 되었습니다.", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
    }

}
