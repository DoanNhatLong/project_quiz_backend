package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.AnswerResponseProjection;
import com.example.ss6_quiz.projection.ChallengerResultProjection;
import com.example.ss6_quiz.projection.ExamGradingProjection;

import java.util.List;

public interface IExamAttemptsService {
    ExamAttempts createExamAttempt(Long examId, Long userId);
    List<AnswerResponseProjection> findByAttemptId(Long attemptId);
    List<ExamAttempts> findAllByUser_Id(Long userId);
    double calculateScore(Long attemptId);
    ChallengerResultProjection findProjectionById(Long Id);
    ExamAttempts findByIdAndStatus(Long id);
}
