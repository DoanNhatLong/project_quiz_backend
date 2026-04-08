package com.example.ss6_quiz.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class RedisTestRunner implements CommandLineRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        try {
            redisTemplate.opsForValue().set("connection_test", "Success");
            String value = redisTemplate.opsForValue().get("connection_test");
            System.out.println(">>> REDIS CONNECTION CHECK: " + value);
        } catch (Exception e) {
            System.err.println(">>> REDIS CONNECTION FAILED: " + e.getMessage());
        }
    }
}
