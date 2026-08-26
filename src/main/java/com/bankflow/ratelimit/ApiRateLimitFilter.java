package com.bankflow.ratelimit;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;


@Component
public class ApiRateLimitFilter extends OncePerRequestFilter {


    private final StringRedisTemplate redisTemplate;

    public ApiRateLimitFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        Authentication authentication = 
            SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (
            authentication != null
            && authentication.isAuthenticated()
        ) {

            String email = authentication.getName();

            String key = "api_rate:" + email;

            Long count = redisTemplate 
                .opsForValue()
                .increment(key);

            if (count == 1) {
                redisTemplate.expire(
                    key,
                    Duration.ofMinutes(1)
                );
            }

            if (count > 100) {
                response.setStatus(
                    HttpStatus.TOO_MANY_REQUESTS.value()
                );
                return;
            }

        }

        filterChain.doFilter(request, response);

    }

}