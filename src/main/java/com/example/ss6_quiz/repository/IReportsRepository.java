package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IReportsRepository extends JpaRepository<Reports, Long> {
    Reports findByAttemptId(Long attemptId);
    List<Reports> findAllByStatusIs(String status);
    @Modifying
    @Transactional
    @Query("UPDATE Reports r SET r.status = CASE WHEN r.status = 'PENDING' THEN 'SOLVED' ELSE 'PENDING' END WHERE r.id = :id")
    int solveReport(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Reports r SET r.isDeleted = true WHERE r.id = :id")
    void softDelete(@Param("id") Long id);
}
