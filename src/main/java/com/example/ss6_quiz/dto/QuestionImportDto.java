package com.example.ss6_quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record QuestionImportDto(
        @JsonProperty("quiz_id")
        Long quizId,

        String content,

        @JsonProperty("order_index")
        Integer orderIndex,

        String type,

        List<AnswerImportDto> answers
) {
    public record AnswerImportDto(
            String content,

            @JsonProperty("is_correct")
            boolean isCorrect
    ) {}
}