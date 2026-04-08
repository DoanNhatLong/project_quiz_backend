package com.example.ss6_quiz.service;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.*;

import java.util.List;
import java.util.Optional;

public interface IExamAttemptsService {
    ExamAttempts createExamAttempt(Long examId, Long userId);
    List<AnswerResponseProjection> findByAttemptId(Long attemptId);
    List<ExamReviewProjection> findAllByUser_Id(Long userId);
    double calculateScore(Long attemptId);
    ChallengerResultProjection findProjectionById(Long Id);
    ExamAttempts findByIdAndStatus(Long id);
    Optional<ExamAttemptProjection> findAttemptById( Long id);

    List<ExamDetailProjection> findExamDetail(Long examId);
}
