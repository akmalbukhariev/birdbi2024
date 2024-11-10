package com.birdbi.api.security.jwt;

import com.birdbi.api.constant.Constant;
import com.birdbi.api.constant.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        log.debug("############## "+path+" ###############");
        if (!path.equals("/haircut/api/v1/user/login") && !path.equals("/haircut/api/v1/admin/login")) {
            // 1. Extracting JWT token from Request Header
            String token = resolveToken(request);

            log.debug("========"+token);
            // 2. Validate token with validateToken
            if (token != null) {
                // If the token is valid, take the Authentication object from the token and store it in SecurityContext.
                Result result = jwtTokenProvider.validateToken(token);

                if(result == Result.SUCCESS) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }else {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                    Map<String, String> error = new HashMap<>();
                    error.put("resultCode", result.getCodeToString());
                    error.put("resultMsg", result.getMessage());
                    try {
                        new ObjectMapper().writeValue(response.getOutputStream(), error);
                    } catch (IOException ignored) {

                    }
                }
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");

                Map<String, String> error = new HashMap<>();
                error.put("resultCode", Result.AUTHENTICATION_ERROR.getCodeToString());
                error.put("resultMsg", Result.AUTHENTICATION_ERROR.getMessage());
                try {
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                } catch (IOException e1) {

                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constant.HEADER_AUTH);

        log.debug("bearerToken==>"+bearerToken);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constant.HEADER_BEARER)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
