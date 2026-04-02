package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Quizzes;
import com.example.ss6_quiz.entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuizzesRepository extends JpaRepository<Quizzes, Long> {
    @Query("SELECT q FROM Quizzes q WHERE q.subject.name = :subjectName AND q.level = :level")
    List<Quizzes> findCustom(String subjectName, Integer level);
    List<Quizzes> findAllBySubject(Subjects subject);
    @Query("SELECT q.subject.id FROM Quizzes q WHERE q.id = :quizId")
    Long findSubjectIdByQuizId(Long quizId);
}
