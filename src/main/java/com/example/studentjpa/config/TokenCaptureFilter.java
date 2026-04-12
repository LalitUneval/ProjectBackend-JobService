package com.example.studentjpa.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

// NO @Component - registered manually in FilterConfig
@Slf4j
public class  TokenCaptureFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        log.info("JWT Token {} "+authHeader);
        
        if (authHeader != null) {
            UserTokenHolder.setToken(authHeader);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            UserTokenHolder.clear();
        }
    }
}
