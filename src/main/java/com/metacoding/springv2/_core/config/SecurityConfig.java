package com.metacoding.springv2._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.metacoding.springv2._core.filter.JwtAuthorizationFilter;
import com.metacoding.springv2._core.util.RespFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encode() {
        // 비밀번호를 데이터베이스에 평문으로 저장하지 않기 위해 BCrypt 해시 알고리즘 사용
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 필터 체인: 요청이 들어오면 거쳐가는 일련의 보안 필터들
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // h2-console 사용 및 iframe 허용 설정
        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        // CORS 설정: 다른 도메인(예: React, Mobile)에서의 자원 요청을 허용하기 위한 설정
        http.cors(cors -> cors.configurationSource(configurationSource()));

        // 인증/인가 실패 시 공통 응답 처리 (RespFilter 활용)
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(
                        (request, response, authException) -> RespFilter.fail(response, 401, "로그인 후 이용해주세요"))
                .accessDeniedHandler(
                        (request, response, accessDeniedException) -> RespFilter.fail(response, 403, "권한이 없습니다")));

        // 세션 정책: JWT를 사용하므로 서버에서 세션을 유지하지 않는 STATELESS 설정 사용
        // 이 설정을 통해 서버의 메모리 부담을 줄이고 확장성을 높임
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // URL별 권한 설정 (인가)
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/api/boards/**").permitAll() // 게시글 조회는 비로그인 허용
                .requestMatchers("/api/**").authenticated()                    // 나머지 api는 인증(JWT) 필수
                .requestMatchers("/admin/**").hasRole("ADMIN")                 // 관리자 전용 주소
                .anyRequest().permitAll());

        // 폼 로그인 및 기본 인증 비활성화: Rest API이므로 JSON 형태의 JWT 인증을 위해 제거
        http.formLogin(f -> f.disable());
        http.httpBasic(b -> b.disable());

        // CSRF 비활성화: 세션을 사용하지 않으므로 CSRF 공격으로부터 상대적으로 안전하며 Rest API 환경에 적합
        http.csrf(c -> c.disable());

        // JWT 인증 필터 등록: UsernamePasswordAuthenticationFilter 전에 실행하여 토큰이 있으면 인증 처리
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 세부 설정
     * 왜? 브라우저의 보안 정책(SOP)에 의해 다른 출처의 API 요청이 차단되는 것을 방지하기 위함
     */
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");  // 모든 헤더 허용
        configuration.addAllowedMethod("*");  // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등) 허용
        configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용 (개발 편의성, 실제 운영 시엔 제한 필요)
        configuration.setAllowCredentials(true); // 쿠키 및 인증 정보 포함 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}