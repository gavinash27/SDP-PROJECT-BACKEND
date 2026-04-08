package com.career.ai_mentor.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Allow public endpoints
        if (path.contains("/student/login") || path.contains("/student/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 TEMP: ALLOW EVERYTHING (NO TOKEN CHECK)
        filterChain.doFilter(request, response);
    }
}