package com.metacoding.springv2._core.filter;

import java.io.IOException;

import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 인가 필터
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("~~~~~~~~~~~~~~~~~sdjalfkjdls;kgf;slkb~~~~~~~~~~~~");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username.equals("ssar") && password.equals("1234")) {
            filterChain.doFilter(request, response);
        } else {
            response.getWriter().print("get out");
        }
    }

}