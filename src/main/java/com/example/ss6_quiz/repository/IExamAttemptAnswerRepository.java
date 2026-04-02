package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ExamAttemptAnswer;
import com.example.ss6_quiz.projection.AnswerResponseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamAttemptAnswerRepository extends JpaRepository<ExamAttemptAnswer, Long> {
    Optional<ExamAttemptAnswer> findByAttemptIdAndQuestionId(Long attemptId, Long questionId);
    List<AnswerResponseProjection> findByAttemptId(Long attemptId);
}
