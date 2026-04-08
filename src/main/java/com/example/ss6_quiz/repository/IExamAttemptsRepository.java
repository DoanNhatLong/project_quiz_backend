package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ExamAttempts;
import com.example.ss6_quiz.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IExamAttemptsRepository extends JpaRepository<ExamAttempts, Long> {
    Optional<ExamAttempts> findByExams_IdAndUser_Id(Long examId, Long userId);
    @Query(value = "SELECT ea.id as id, ea.user_id as userId, ea.exam_id as examId, " +
                   "c.title as title, c.start_time as startTime, c.status as status " +
                   "FROM exam_attempts ea " +
                   "JOIN challenges c ON c.exam_id = ea.exam_id " +
                   "WHERE ea.user_id = :userId and c.status='ENDED'", nativeQuery = true)
    List<ExamReviewProjection> findAllByUser_Id(@Param("userId") Long userId);

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
    @Query(value = """
    SELECT 
        id as id, 
        exam_id as examId, 
        started_at as startedAt,
        total_score as totalScore
    
    FROM exam_attempts 
    WHERE id = :id
    """, nativeQuery = true)
    Optional<ExamAttemptProjection> findAttemptById(@Param("id") Long id);

    @Query(value = "SELECT ea.exam_id as examId, " +
                   "ea.total_score as totalScore, " +
                   "ea.is_passed as isPassed, " +
                   "u.username as username, " +
                   "u.email as email " +
                   "FROM exam_attempts ea " +
                   "JOIN users u ON ea.user_id = u.id " +
                   "JOIN challenges c ON c.exam_id = ea.exam_id " +
                   "WHERE ea.exam_id = :examId " +
                   "AND ea.completed_at > c.start_time " +
                   "AND ea.completed_at < DATE_ADD(c.start_time, INTERVAL c.duration_minutes MINUTE)",
            nativeQuery = true)
    List<ExamDetailProjection> findExamDetail(@Param("examId") Long examId);



}
