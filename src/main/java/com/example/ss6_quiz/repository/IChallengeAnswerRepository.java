package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ChallengeAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChallengeAnswerRepository extends JpaRepository<ChallengeAnswer, Long> {
}
