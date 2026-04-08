package com.example.ss6_quiz.projection;

public interface ExamDetailProjection {
    Long getExamId();
    Double getTotalScore();
    Boolean getIsPassed();
    String getUsername();
    String getEmail();
}
