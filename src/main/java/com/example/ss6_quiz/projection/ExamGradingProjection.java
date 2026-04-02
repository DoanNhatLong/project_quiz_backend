package com.example.ss6_quiz.projection;

public interface ExamGradingProjection {
    Long getQuestionId();
    String getCorrectIds();
    String getSelectedIds();
    Double getPassScore();
}
