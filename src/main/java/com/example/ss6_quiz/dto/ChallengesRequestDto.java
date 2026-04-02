package com.example.ss6_quiz.dto;

public record ChallengesRequestDto(
        String title,
        Long examId,
        Long userId,
        String accessCode,
        Integer durationMinutes,
        String startTime,
        Boolean allowRejoin
) {
}
