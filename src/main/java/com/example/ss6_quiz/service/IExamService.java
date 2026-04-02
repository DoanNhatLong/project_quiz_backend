package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ExamRequestDto;
import com.example.ss6_quiz.dto.TempExamRequestDto;
import com.example.ss6_quiz.entity.Exams;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.projection.ExamQuestionProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IExamService {
    Exams createExam(ExamRequestDto dto);
    List<Exams> getAllExams();

    Exams getExamById(Long examId);
    Page<Questions> getExamQuestions(Long examId, Pageable pageable);

    void deleteExam(Long examId);

    List<ExamQuestionProjection> getAllExamQuestions(Long examId);

    void createTempExam(TempExamRequestDto payload);
}
