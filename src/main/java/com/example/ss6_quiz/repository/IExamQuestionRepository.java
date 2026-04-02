package com.example.ss6_quiz.repository;

import com.example.ss6_quiz.entity.ExamQuestion;
import com.example.ss6_quiz.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {
    @Query(value = """
            SELECT eq.question FROM ExamQuestion eq WHERE eq.exams.id = :examId
            """)
    Page<Questions> findQuestionsByExamId(@Param("examId") Long examId, Pageable pageable);
}
