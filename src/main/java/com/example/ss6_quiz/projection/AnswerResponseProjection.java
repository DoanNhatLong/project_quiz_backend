package com.example.ss6_quiz.projection;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface AnswerResponseProjection {
    Long getQuestionId();
    String getSelectedOptionId();

    default List<Long> getSelectedOptionIds() {
        String raw = getSelectedOptionId();
        if (raw == null || raw.isBlank()) {
            return Collections.emptyList();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .map(Long::valueOf)
                .toList();
    }
}
