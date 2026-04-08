package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.AdminLogs;
import com.example.ss6_quiz.projection.CountStatsProjection;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IAdminLogsRepository extends JpaRepository<AdminLogs,Long> {
    List<AdminLogs> findAllByActionTypeContaining(String actionType, Sort sort);
    @Query(value = "SELECT " +
                   "(SELECT COUNT(*) FROM users) AS totalUser, " +
                   "(SELECT COUNT(*) FROM questions) AS totalQuestion, " +
                   "(SELECT COUNT(*) FROM quizzes) AS totalQuizzes",
            nativeQuery = true)
    CountStatsProjection getCountStats();

    @Modifying
    @Transactional
    @Query("UPDATE AdminLogs r SET r.isDeleted = true WHERE r.id = :id")
    void softDelete(@Param("id") Long id);
}
