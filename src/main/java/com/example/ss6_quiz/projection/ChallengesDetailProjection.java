package com.example.ss6_quiz.projection;

import java.time.LocalDateTime;

public interface ChallengesDetailProjection {
    Long getId();

    String getTitle();

    String getAccessCode();

    Long getExamId();

    Integer getDurationMinutes();

    LocalDateTime getStartTime();
}
