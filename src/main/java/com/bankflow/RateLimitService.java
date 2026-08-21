package com.bankflow;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final StringRedisTemplate redisTemplate;

    public RateLimitService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public boolean isAllowed(String email) {

        String key = "login_attempts:" + email;

        Long attempts = redisTemplate
            .opsForValue()
            .increment(key);

        if (attempts == 1) {
            redisTemplate.expire(
                key,
                Duration.ofMinutes(1)
            );
        }

        return attempts <= 5;

    }

    public void resetAttempts(String email) {

        String key = "login_attempts:" + email;

        redisTemplate.delete(key);
    }

}