package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.ChallengerResultProjection;
import com.example.ss6_quiz.projection.ExamGradingProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamAttemptsRepository extends JpaRepository<ExamAttempts, Long> {
    Optional<ExamAttempts> findByExams_IdAndUser_Id(Long examId, Long userId);
    List<ExamAttempts> findAllByUser_Id(Long userId);
    @Query(value = """
        WITH TargetQuestions AS (
             SELECT eq.question_id
             FROM exam_questions eq
             JOIN exam_attempts ea ON eq.exam_id = ea.exam_id
             WHERE ea.id = :attemptId
        ),
        CorrectAnswer AS (
             SELECT question_id, GROUP_CONCAT(id ORDER BY id ASC SEPARATOR ',') AS correct_ids
             FROM answers
             WHERE is_correct = true
             AND question_id IN (SELECT question_id FROM TargetQuestions)
             GROUP BY question_id
        ), 
        UserSelection AS (
            SELECT question_id, GROUP_CONCAT(selected_option_id ORDER BY selected_option_id ASC SEPARATOR ',') AS selected_ids
            FROM exam_attempt_answers
            WHERE attempt_id = :attemptId
            GROUP BY question_id
        )
        SELECT ca.question_id as questionId, 
               ca.correct_ids as correctIds, 
               us.selected_ids as selectedIds
        
        FROM CorrectAnswer ca
        LEFT JOIN UserSelection us ON ca.question_id = us.question_id
        """, nativeQuery = true)
    List<ExamGradingProjection> findGradingDataByAttemptId(@Param("attemptId") Long attemptId);
    ChallengerResultProjection findProjectionById(Long Id);
    ExamAttempts findByIdAndStatus(Long id, String status);

}
