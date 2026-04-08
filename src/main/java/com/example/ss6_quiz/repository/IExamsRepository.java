package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Exams;
import com.example.ss6_quiz.projection.ExamQuestionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExamsRepository extends JpaRepository<Exams,Long> {
    @Query(value = "SELECT e.id, q.id as question_id, q.content, q.type, a.content as answer, a.id as answer_id " +
                   "FROM exams e " +
                   "JOIN exam_questions es ON e.id = es.exam_id " +
                   "JOIN questions q ON q.id = es.question_id " +
                   "JOIN answers a ON q.id = a.question_id where e.id = :examId",
            nativeQuery = true)
    List<ExamQuestionProjection> findAllExamQuestionsNative(@Param("examId") Long examId);

    @Query("SELECT e.durationMinutes FROM Exams e WHERE e.id = :examId")
    Integer findDurationByExamId(@Param("examId") Long examId);
}
