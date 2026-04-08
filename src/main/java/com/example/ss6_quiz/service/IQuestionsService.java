package com.example.ss6_quiz.service;

import com.example.ss6_quiz.annotation.AdminActionLog;
import com.example.ss6_quiz.dto.QuestionUploadDto;
import com.example.ss6_quiz.dto.QuestionsRequestDto;
import com.example.ss6_quiz.dto.QuestionsResponseDto;
import com.example.ss6_quiz.entity.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IQuestionsService {
    List<QuestionsResponseDto> getAll();
    List<QuestionsResponseDto> getByQuizId(Long quizId);
    QuestionsResponseDto getById(Long id);
    QuestionsResponseDto create(QuestionsRequestDto dto);

    QuestionsResponseDto update(Long id, QuestionsRequestDto dto);
    void delete(Long id);
    List<Questions> findRandom10ByQuizId(Long quizId);
    void saveQuestion (QuestionUploadDto dto);
    void importAll(List<QuestionUploadDto> dtoList);
    Page<Questions> getAllQuestionExam(Pageable pageable, String content, Long quizId);
    Questions getQuestionById(Long id);

    void saveFull(Long id, Questions dto);
}
