package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.QuizAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizAttemptsRepository extends JpaRepository<QuizAttempts, Long> {
    List<QuizAttempts> findAllByUserId(Long userId);
}
