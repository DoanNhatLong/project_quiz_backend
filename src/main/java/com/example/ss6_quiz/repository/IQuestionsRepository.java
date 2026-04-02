package com.example.ss6_quiz.repository;
import com.example.ss6_quiz.dto.QuestionUploadDto;
import com.example.ss6_quiz.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionsRepository extends JpaRepository<Questions, Long> {
    @Query(value = """
    SELECT * FROM questions
    WHERE quiz_id = :quizId
    AND is_deleted = 0
    ORDER BY RAND()
    LIMIT 10
    """, nativeQuery = true)
    List<Questions> findRandom10ByQuizId(@Param("quizId") Long quizId);
    List<Questions> findByQuizId(Long quizId);
    int countByQuiz_Id(Long quizId);
    List<Questions> findAllByQuizId(Long quizId);
    @Query("SELECT COALESCE(MAX(q.orderIndex), 0) FROM Questions q WHERE q.quiz.id = :quizId")
    Integer findMaxOrderIndexByQuizId(Long quizId);
    Page<Questions> findAllByQuizId(Long quizId, Pageable pageable);
    Page<Questions> findAllByQuizIdAndContentContaining(Long quizId, String content, Pageable pageable);

}
