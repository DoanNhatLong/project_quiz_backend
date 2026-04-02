package com.example.ss6_quiz.dto;

import java.util.List;

public record ExamRequestDto(
        String title,
        String description,
        Integer durationMinutes,
        Float passScorePercentage,
        Long userId,
        List<Long> questionIds
) {}
