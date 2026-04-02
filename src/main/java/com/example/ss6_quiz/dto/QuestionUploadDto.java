package com.example.ss6_quiz.dto;

import java.util.List;

public record QuestionUploadDto(
        Long quiz_id,
        String content,
        String type,
        List<AnswerDto> answers
) {
    public record AnswerDto(
            String content,
            boolean is_correct
    ) {}
}