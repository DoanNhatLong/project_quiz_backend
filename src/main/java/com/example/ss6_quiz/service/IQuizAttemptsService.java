package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.QuizResultResponseDto;
import com.example.ss6_quiz.entity.QuizAttempts;

import java.util.List;
import java.util.Map;

public interface IQuizAttemptsService {
    QuizAttempts startAttempt(QuizAttempts attempt); // Lưu lúc bắt đầu thi
    QuizAttempts submitAttempt(Long id, Double score, boolean isPassed); // Cập nhật lúc nộp bài
    List<QuizAttempts> getHistoryByUser(Long userId);
    QuizAttempts startNewAttempt(Long userId, Long quizId);
    QuizResultResponseDto submitQuiz(Long attemptId, Map<Long, List<Long>> answers);

}
