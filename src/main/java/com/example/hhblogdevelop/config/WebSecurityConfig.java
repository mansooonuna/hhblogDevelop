package com.example.hhblogdevelop.config;


import com.example.hhblogdevelop.jwt.JwtAuthFilter;
import com.example.hhblogdevelop.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.hhblogdevelop.exception.ErrorCode.INVALID_AUTH_TOKEN;
import static com.example.hhblogdevelop.exception.ErrorCode.USER_NOT_FOUND;
import static com.example.hhblogdevelop.exception.ErrorResponse.toResponseEntity;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 어노테이션 활성화
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private static final String[] PERMIT_URL_ARRAY = {
/* swagger v2 */
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
/* swagger v3 */
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // resources 접근 허용 설정
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                // login 없이 허용하는 페이지
                .antMatchers("/api/posts/**").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers(PERMIT_URL_ARRAY).permitAll()
//                .antMatchers("api/post").hasRole("ADMIN")
                // 어떤 요청이든 '인증'
                .anyRequest().authenticated();


        http.exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {

                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response,
                                         AuthenticationException authException) throws IOException, ServletException {
                        toResponseEntity(USER_NOT_FOUND);
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {

                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response,
                                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        toResponseEntity(INVALID_AUTH_TOKEN);
                    }
                })
        ;

        // JWT 인증/인가를 사용하기 위한 설정
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.httpBasic().and();

        return http.build();
    }


}