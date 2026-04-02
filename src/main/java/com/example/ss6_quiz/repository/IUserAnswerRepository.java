package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByAttempt_Id(Long attemptId);
}
