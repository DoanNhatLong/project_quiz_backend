package com.example.ss6_quiz.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface ExamAttemptProjection {
    Long getId();
    Long getExamId();
    LocalDateTime getStartedAt();
    Double getTotalScore();
}
