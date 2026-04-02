package com.example.ss6_quiz.projection;

public interface ChallengerResultProjection {
    Long getId();
    Double getTotalScore();
    Boolean getIsPassed();
    String getStatus();
}
