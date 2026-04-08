package com.example.ss6_quiz.service;

import com.example.ss6_quiz.dto.ExamRequestDto;
import com.example.ss6_quiz.dto.TempExamRequestDto;
import com.example.ss6_quiz.entity.*;
import com.example.ss6_quiz.projection.ExamQuestionProjection;
import com.example.ss6_quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamsService implements IExamService {
    @Autowired
    IExamsRepository examsRepository;
    @Autowired
    IUsersRepository usersRepository;
    @Autowired
    IQuestionRepository questionRepository;
    @Autowired
    IExamQuestionRepository examQuestionRepository;
    @Autowired
    IExamAttemptAnswerRepository answerRepository;

    @Override
    public Exams createExam(ExamRequestDto dto) {
        Users user = usersRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Exams exam = new Exams();
        exam.setTitle(dto.title());
        exam.setDescription(dto.description());
        exam.setDurationMinutes(dto.durationMinutes());
        exam.setPassScorePercentage(dto.passScorePercentage());
        exam.setUser(user);
        exam.setCreatedAt(LocalDateTime.now());

        List<ExamQuestion> examQuestions = dto.questionIds().stream().map(qId -> {
            Questions question = questionRepository.findById(qId)
                    .orElseThrow(() -> new RuntimeException("Question not found ID: " + qId));

            ExamQuestion eq = new ExamQuestion();
            eq.setExams(exam);
            eq.setQuestion(question);
            return eq;
        }).collect(Collectors.toList());

        exam.setExamQuestions(examQuestions);

        return examsRepository.save(exam);
    }

    @Override
    public List<Exams> getAllExams() {
        return examsRepository.findAll();
    }

    @Override
    public Exams getExamById(Long examId) {
        return examsRepository.findById(examId).orElse(null);
    }

    @Override
    public Page<Questions> getExamQuestions(Long examId, Pageable pageable) {
        return examQuestionRepository.findQuestionsByExamId(examId, pageable);
    }

    @Override
    public void deleteExam(Long examId) {
        Exams exam = examsRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));
        exam.setDeleted(true);
        examsRepository.save(exam);
    }

    @Override
    public List<ExamQuestionProjection> getAllExamQuestions(Long examId) {
        return examsRepository.findAllExamQuestionsNative(examId);
    }

    @Override
    @Transactional
    public void createTempExam(TempExamRequestDto payload) {
        Long attemptId = Long.parseLong(payload.attemptId());
        for (var answerData : payload.data()) {
            Long qId = answerData.questionId().longValue();

            ExamAttemptAnswer entity = answerRepository
                    .findByAttemptIdAndQuestionId(attemptId, qId)
                    .orElse(new ExamAttemptAnswer());

            String joinedIds = answerData.selectedOptionIds().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            entity.setAttemptId(attemptId);
            entity.setQuestionId(qId);
            entity.setSelectedOptionId(joinedIds);
            entity.setIsCorrect(true);
            answerRepository.save(entity);
        }
    }

    @Override
    public Integer findDurationByExamId(Long examId) {
        return examsRepository.findDurationByExamId(examId);
    }

}
