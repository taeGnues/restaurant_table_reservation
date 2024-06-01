package com.zerobase.tablereservation.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveTokenFromRequest(request);

        if(StringUtils.hasText(token) && provider.validateToken(token)){
            Authentication auth = provider.getAuthentication(token);
            SecurityContextHolder.getContext()
                    .setAuthentication(auth); // 인증정보 등록하기.
            log.info("{} -> ", SecurityContextHolder.getContext().getAuthentication());
            log.info(String.format("[%s] ->", request.getRequestURI()));
        }

        filterChain.doFilter(request, response);
    }

    private static String resolveTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            log.info("-> {}", token);
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}
