package com.example.ss6_quiz.projection;

public interface ExamQuestionProjection {
    Long getId();
    Long getQuestion_id();
    String getContent();
    String getType();
    String getAnswer();
    Long getAnswer_id();
}
