package com.mbproyect.campusconnect.config.auth;


import com.mbproyect.campusconnect.service.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter (JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtService.extractAuthToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractCredentials(token);

        // Validate token if is not authenticated yet
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtService.validateToken(token, email)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email, null, Collections.emptyList()
                        );

                // Provides request details (session id, IP, etc)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication to security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}

