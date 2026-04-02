package com.example.ss6_quiz.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExamResponseDto(
        Long id,
        String title,
        String description,
        Integer durationMinutes,
        Float passScorePercentage,
        LocalDateTime createdAt,
        String authorName,
        List<ExamQuestionResponse> questions
) {
    public record ExamQuestionResponse(
            Long id,
            String content,
            String type
    ) {
    }
}



