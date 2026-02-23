package com.metacoding.springv2._core.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.metacoding.springv2._core.util.JwtProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT 기반의 인가 처리를 담당하는 필터.
 * <p>
 * {@link OncePerRequestFilter}를 상속받아 하나의 요청당 단 한 번만 실행됨을 보장함.
 * 모든 API 요청 시 헤더에서 JWT를 추출하고, 검증에 성공하면 시큐리티 세션에 인증 정보를 등록하는 역할.
 * </p>
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 요청 헤더(Authorization)에서 Bearer 접두어를 제외한 순수 JWT 토큰 추출
        String jwt = JwtProvider.토큰추출하기(request);

        // 2. 토큰이 존재한다면 (비인증 주소 요청 시에는 null일 수 있음)
        if (jwt != null) {
            // 3. 토큰 검증 및 인증 객체(Authentication) 생성
            // 내부적으로 JWT 서명을 확인하고, 클레임(정보)을 파싱하여 User 객체와 연결함.
            Authentication authentication = JwtProvider.인증객체만들기(jwt);
            
            // 4. 시큐리티 세션(SecurityContext)에 인증 정보 저장
            // 이로써 이후 컨트롤러 등에서 @AuthenticationPrincipal 등을 통해 사용자 정보를 꺼내 쓸 수 있음.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. 다음 필터 체인(예: UsernamePasswordAuthenticationFilter 또는 Controller)으로 요청 전달
        filterChain.doFilter(request, response);
    }
}