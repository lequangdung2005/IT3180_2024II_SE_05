package com.hust.ittnk68.cnpm.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

// import org.springframework.stereotype.*;
import com.hust.ittnk68.cnpm.security.TokenAuthentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// @Component
public class TokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // remove "Bearer "
            SecurityContextHolder.getContext().setAuthentication(new TokenAuthentication(token));   // store class in security context
        }
        chain.doFilter(request, response);
    }
}
