package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.QuizRequestDto;
import com.example.ss6_quiz.entity.Questions;
import com.example.ss6_quiz.entity.Quizzes;
import com.example.ss6_quiz.entity.Subjects;
import com.example.ss6_quiz.repository.IQuestionsRepository;
import com.example.ss6_quiz.repository.IQuizzesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizzesService implements IQuizzesService {
    @Autowired
    private IQuizzesRepository quizzesRepository;
    @Autowired
    private IQuestionsRepository questionsRepository;
    @Autowired
    private ISubjectsService subjectsService;

    @Override
    public List<Quizzes> getAllQuizzes() {
        return quizzesRepository.findAll();
    }

    @Override
    public Quizzes getQuizById(Long id) {
        return quizzesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bộ câu hỏi này!"));
    }

    @Override
    public Quizzes createQuiz(Quizzes quiz) {
        return quizzesRepository.save(quiz);
    }

    @Override
    public Quizzes updateQuiz(Long id, Quizzes quizDetails) {
        Quizzes quiz = getQuizById(id);
        quiz.setTitle(quizDetails.getTitle());
        quiz.setDescription(quizDetails.getDescription());
        quiz.setPassScore(quizDetails.getPassScore());
        quiz.setSubject(quizDetails.getSubject());
        quiz.setLevel(quizDetails.getLevel());
        return quizzesRepository.save(quiz);
    }

    @Override
    public void deleteQuiz(Long id) {
        Quizzes quiz = getQuizById(id);
        quiz.setDeleted(true);
        quizzesRepository.save(quiz);
    }

    @Override
    public List<Questions> findAllByQuizId(Long quizId) {
        return questionsRepository.findAllByQuizId(quizId);
    }

    @Override
    public Quizzes createQuiz(QuizRequestDto dto) {
        Quizzes quiz = new Quizzes();
        quiz.setTitle(dto.title());
        quiz.setDescription(dto.description());
        quiz.setPassScore(dto.passScore() != null ? dto.passScore().floatValue() : 0.5f);
        if (dto.language() != null) {
            quiz.setSubject(subjectsService.findByName(dto.language().toUpperCase()));
        }

        quiz.setLevel(dto.level() != null ? dto.level() : 1);
        return quizzesRepository.save(quiz);
    }

    @Override
    public List<Quizzes> findAllBySubjectAndLevel(String subject, Integer level) {
        return quizzesRepository.findCustom(subject, level);
    }

    @Override
    public List<Quizzes> findAllBySubject(Subjects subject) {
        return quizzesRepository.findAllBySubject(subject);
    }

    @Override
    public Long getSubjectIdByQuizId(Long quizId) {
        return quizzesRepository.findSubjectIdByQuizId(quizId);
    }
}
