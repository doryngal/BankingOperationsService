package com.example.BankingOperationsService.config;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RedisConfig {
    @Resource
    RedisTemplate<String, String> redisTemplate;


    public String get(String key) {
        if ("".equals(key)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value) {
        if ("".equals(key) || "".equals(value)) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);
    }
}