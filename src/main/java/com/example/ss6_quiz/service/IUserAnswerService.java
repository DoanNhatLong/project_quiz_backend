package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.UserAnswer;

import java.util.List;

public interface IUserAnswerService {
    List<UserAnswer> findByAttempt_Id(Long attemptId);
}
