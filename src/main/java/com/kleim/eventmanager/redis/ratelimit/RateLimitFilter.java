package com.kleim.eventmanager.redis.ratelimit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final FixedWindowRateLimiter fixedWindowRateLimiter;

    public RateLimitFilter(FixedWindowRateLimiter fixedWindowRateLimiter) {
        this.fixedWindowRateLimiter = fixedWindowRateLimiter;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        if (!isLoginRequests(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String client = Optional.ofNullable(request.getHeader("X-API-KEY"))
                .filter(s -> !s.isEmpty())
                .orElseGet(() -> Optional.ofNullable(request.getRemoteAddr()).orElse("unknown"));

    boolean allow = fixedWindowRateLimiter.allowRequest(client, 10, Duration.ofMinutes(1));
    if (!allow) {
        response.setStatus(429); //TOO_MANY_REQUESTS
        response.setContentType("application/jsom");
        response.getWriter().write("Rate limit exceeded");
        return;
    }

    filterChain.doFilter(request, response);
   }

   private boolean isLoginRequests(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod()) && "/api/users/auth".equals(request.getRequestURI());
   }
}
