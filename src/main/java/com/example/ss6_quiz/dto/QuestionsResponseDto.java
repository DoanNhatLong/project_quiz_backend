package com.example.ss6_quiz.dto;

import com.example.ss6_quiz.entity.Questions.QuestionType;
import java.time.LocalDateTime;

public record QuestionsResponseDto(
        Long id,
        Long quizId,
        String quizTitle,
        String content,
        QuestionType type,
        Integer orderIndex,
        LocalDateTime createdAt
) {}