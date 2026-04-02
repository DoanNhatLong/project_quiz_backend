package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAnswerRepository extends JpaRepository<Answers, Long> {
    List<Answers> findByQuestion_IdAndIsCorrectTrue(Long questionId);
}
