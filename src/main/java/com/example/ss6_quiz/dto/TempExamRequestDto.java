package com.example.ss6_quiz.dto;

import java.util.List;

public record TempExamRequestDto(
        String attemptId,
        List<AnswerData> data
) {
    public record AnswerData(
            Integer questionId,
            List<Integer> selectedOptionIds
    ) {}
}