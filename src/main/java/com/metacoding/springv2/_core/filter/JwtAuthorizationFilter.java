package com.metacoding.springv2._core.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.metacoding.springv2._core.util.JwtUtil;
import com.metacoding.springv2.user.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인가 필터
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // localhost:8080/api/good(모든 주소에서 동작)
        // header -> Authorization : Bearer JWT토큰

        String jwt = request.getHeader("Authorization");

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzc2FyIiwicm9sZXMiOiJVU0VSIiwiaWQiOjEsImV4cCI6MTc3MjA4ODUwOH0.5ro3tx-CHw57q9BUfZ-S_ometEd4E-rCzfS89SQOLLkFIGdgnyujAgahfqQq8k-9bQsiJXbcjIyXYRoFJdo9cA
        jwt = jwt.replace("Bearer ", "");

        // id: 1, roles: user, username: ssar
        User user = JwtUtil.verify(jwt);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}