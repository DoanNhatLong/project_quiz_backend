package com.example.ss6_quiz.dto;

public record QuizResultResponseDto(
        int correct,
        int total,
        double score
) {}
