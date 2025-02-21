package com.sparta.levelup_backend.domain.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final int LIMIT = 3;
    private static final long MINUTE = 5;

    public boolean isRequest(Long userId) {
        String key = "cancel_request:" + userId;
        Integer request = (Integer) redisTemplate.opsForValue().get(key);

        if (request == null) {
            redisTemplate.opsForValue().set(key, 1, MINUTE, TimeUnit.MINUTES);
            return true;
        }

        if (request < LIMIT) {
            redisTemplate.opsForValue().increment(key);
            return true;
        }

        return false;
    }
}
