package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ExamSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IExamSnapshotRepository extends JpaRepository<ExamSnapshot, Long> {

        @Query(value = "SELECT s.snapshot_data FROM exam_snapshots s " +
                       "WHERE s.exam_id = (SELECT a.exam_id FROM exam_attempts a WHERE a.id = :attemptId) " +
                       "AND s.user_id = (SELECT a.user_id FROM exam_attempts a WHERE a.id = :attemptId)",
                nativeQuery = true)
        Optional<String> findRawDataByAttemptId(@Param("attemptId") Long attemptId);
    boolean existsByExamIdAndUserId(Long examId, Long userId);


}
