package com.juneve.letterdiary.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 (추후 구현 예정)
 * - 요청 헤더에서 JWT 토큰 추출
 * - 토큰 검증 후 SecurityContext에 인증 정보 저장
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        // TODO: JWT 토큰 검증 로직 추가
        SecurityContextHolder.clearContext();

        filterChain.doFilter(request, response);
    }
}
