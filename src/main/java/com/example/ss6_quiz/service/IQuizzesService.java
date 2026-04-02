package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.QuizRequestDto;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.entity.Quizzes;
import com.example.ss6_quiz.entity.Subjects;

import java.util.List;

public interface IQuizzesService {
    List<Quizzes> getAllQuizzes();
    Quizzes getQuizById(Long id);
    Quizzes createQuiz(Quizzes quiz);
    Quizzes updateQuiz(Long id, Quizzes quizDetails);
    void deleteQuiz(Long id);
    List<Questions> findAllByQuizId(Long quizId);
    Quizzes createQuiz(QuizRequestDto dto);
    List<Quizzes> findAllBySubjectAndLevel(String subject, Integer level);
    List<Quizzes> findAllBySubject(Subjects subject);
    Long getSubjectIdByQuizId(Long quizId);
}
