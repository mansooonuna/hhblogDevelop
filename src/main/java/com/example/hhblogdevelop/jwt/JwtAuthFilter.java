package com.example.hhblogdevelop.jwt;

import com.example.hhblogdevelop.dto.SecurityExceptionDto;
import com.example.hhblogdevelop.entity.Users;
import com.example.hhblogdevelop.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    // HTTP 요청이 오면 WAS(tomcat)가 HttpServletRequest, HttpServletResponse 객체를 만들어 줍니다.
    // 만든 인자 값을 받아옵니다.
    // 요청이 들어오면 diFilterInternal 이 딱 한번 실행된다.
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // WebSecurityConfig 에서 보았던 UsernamePasswordAuthenticationFilter 보다 먼저 동작을 하게 됩니다.

        // JWT 토큰을 해석하여 추출
        String access_token = jwtUtil.resolveToken(request, JwtUtil.ACCESS_KEY);
        String refresh_token = jwtUtil.resolveToken(request, JwtUtil.REFRESH_KEY);

        // 토큰이 존재하면 유효성 검사를 수행하고, 유효하지 않은 경우 예외 처리
        if (access_token != null) {
            if (jwtUtil.validateToken(access_token)) {
                setAuthentication(jwtUtil.getUserInfoFromToken(access_token));
            } else if (refresh_token != null && jwtUtil.refreshTokenValid(refresh_token)) {
                //Refresh토큰으로 유저명 가져오기
                String username = jwtUtil.getUserInfoFromToken(refresh_token);
                //유저명으로 유저 정보 가져오기
                Users user = userRepository.findByUsername(username).get();
                //새로운 ACCESS TOKEN 발급
                String newAccessToken = jwtUtil.createToken(username, user.getRole(), "Access");
                //Header에 ACCESS TOKEN 추가
                jwtUtil.setHeaderAccessToken(response, newAccessToken);
                setAuthentication(username);
            } else if (refresh_token == null) {
                jwtExceptionHandler(response, "AccessToken Expired.", HttpStatus.BAD_REQUEST.value());
                return;
            } else {
                jwtExceptionHandler(response, "RefreshToken Expired.", HttpStatus.BAD_REQUEST.value());
                return;
            }
        }

        // 다음 필터로 요청과 응답을 전달하여 필터 체인 계속 실행
        filterChain.doFilter(request, response);
    }

    // 인증 객체를 생성하여 SecurityContext에 설정
    public void setAuthentication(String username) {
        Authentication authentication = jwtUtil.createAuthentication(username);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // security가 만드는 securityContextHolder 안에 authentication을 넣음.
        // security가 sicuritycontxxtholder에서 인증 객체를 확인.
        // JwtAuthFilter에서 authentication을 넣어주면 UsernamePasswordAuthenticationFilter 내부에서 인증이 된 것을 확인하고 추가적인 작업을 진행하지 않음.
    }

    // JWT 예외 처리를 위한 응답 설정
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            // 예외 정보를 JSON 형태로 변환하여 응답에 작성
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}