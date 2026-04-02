package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReportsRepository extends JpaRepository<Reports, Long> {
}
