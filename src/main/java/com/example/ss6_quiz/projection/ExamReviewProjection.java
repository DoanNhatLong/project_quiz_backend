package com.example.ss6_quiz.projection;

import java.time.LocalDateTime;

public interface ExamReviewProjection {
    Long getId();
    Long getUserId();
    Long getExamId();
    String getTitle();
    LocalDateTime getStartTime();
    String getStatus();
}
