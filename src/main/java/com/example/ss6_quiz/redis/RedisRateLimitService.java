package com.example.ss6_quiz.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class RedisRateLimitService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int MAX_CHALLENGER_PER_DAY = 3;

    public boolean canCreateExam(Long teacherId) {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String key = "limit:exam:teacher:" + teacherId + ":date:" + today;

        String currentCountStr = redisTemplate.opsForValue().get(key);
        int currentCount = (currentCountStr == null) ? 0 : Integer.parseInt(currentCountStr);

        if (currentCount >= MAX_CHALLENGER_PER_DAY) {
            return false;
        }

        Long newCount = redisTemplate.opsForValue().increment(key);

        if (newCount != null && newCount == 1) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }

        return true;
    }
}
