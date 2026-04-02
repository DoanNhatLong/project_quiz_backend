package com.example.ss6_quiz.dto;

import com.example.ss6_quiz.entity.Questions.QuestionType;

public record QuestionsRequestDto(
        Long quizId,
        String content,
        QuestionType type,
        Integer orderIndex
) {}